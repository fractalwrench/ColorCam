package com.fractalwrench.colorcam.ui

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.graphics.Palette
import com.fractalwrench.colorcam.image.BitmapRepository
import com.fractalwrench.colorcam.image.PaletteColors
import com.fractalwrench.crazycats.image.Presenter
import com.fractalwrench.crazycats.injection.DefaultSchedulers
import io.reactivex.Observable

class CameraActivityPresenter(val schedulers: DefaultSchedulers, val repository: BitmapRepository) : Presenter<CameraView>() {

    private var cameraAvailable = false
    private var galleryResultObservable: Observable<Bitmap>? = null

    override fun start(view: CameraView) {
        super.start(view)
        galleryResultObservable?.let { displayCapturedImage(it) }
    }

    fun onRateClicked() {
        view?.launchRate()
    }

    fun onGalleryClicked() {
        view?.launchGallery()
    }

    fun onCaptureClicked() {
        if (cameraAvailable) {
            view?.capturePhoto()
        } else {
            view?.displayCameraPreview()
        }
    }

    fun onCameraStateChanged(available: Boolean) {
        cameraAvailable = available
    }

    fun onCameraBitmapCaptured(jpeg: ByteArray?) {
        val bmpSource = Observable.just(jpeg)
                .map { BitmapFactory.decodeByteArray(jpeg, 0, jpeg?.size!!) }

        displayCapturedImage(bmpSource)
    }


    fun handleGalleryResult(data: Intent?, ctx: Context) {
//        presenter not started at this point, hold reference in field and subscribe in start()
        galleryResultObservable = Observable.just(data?.data)
                .map { ctx.contentResolver.openInputStream(it) }
                .map { BitmapFactory.decodeStream(it) }
    }


    // Private methods


    private fun displayCapturedImage(source: Observable<Bitmap>) {
        val observable = source.map {
            val palette = Palette.from(it).generate()
            Pair(it, PaletteColors(palette))
        }
                .compose { schedulers.apply(it) }

        compositeDisposable?.add(observable.subscribe({
            view?.displayCapturedImage(it.first, it.second)
        }, {
            view?.displayCameraPreview()
            TODO()
        }))
    }

}
