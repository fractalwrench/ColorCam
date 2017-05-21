package com.fractalwrench.colorcam.ui

import android.graphics.BitmapFactory
import android.support.v7.graphics.Palette
import com.fractalwrench.colorcam.image.BitmapRepository
import com.fractalwrench.colorcam.image.PaletteColors
import com.fractalwrench.crazycats.image.Presenter
import com.fractalwrench.crazycats.injection.DefaultSchedulers
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class CameraActivityPresenter(val schedulers: DefaultSchedulers, val repository: BitmapRepository) : Presenter<CameraView>() {

    private val captureInterval: Long = 3
    private var cameraDisposable: Disposable? = null

    fun onRateClicked() {
        contentView?.displayPlayStoreListing()
    }

    fun onGalleryClicked() {
        contentView?.launchGallery()
    }

    fun onCaptureClicked() {
        contentView?.captureImagePreview()
    }

    fun onCameraStateChanged(available: Boolean) {
        if (available) {
            cameraDisposable = Observable.interval(1, captureInterval, TimeUnit.SECONDS)
                    .map { contentView?.captureImagePreview() }
                    .toFlowable(BackpressureStrategy.DROP)
                    .subscribe()
        } else {
            cameraDisposable?.dispose()
        }
    }

    fun onBitmapCreated(jpeg: ByteArray?) {
        val observable = Observable.just(jpeg)
                .map { BitmapFactory.decodeByteArray(jpeg, 0, jpeg?.size!!) }
                .map {
                    val palette = Palette.from(it).generate()
                    it.recycle()
                    palette
                }
                .map(::PaletteColors)
                .compose { schedulers.apply(it) }

        compositeDisposable?.add(observable.subscribe({
            contentView?.updatePaletteColors(it)
        }, {
            it?.printStackTrace() // TODO
        }))
    }

}
