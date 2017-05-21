package com.fractalwrench.colorcam.ui

import android.graphics.BitmapFactory
import android.support.v7.graphics.Palette
import com.fractalwrench.colorcam.image.BitmapRepository
import com.fractalwrench.colorcam.image.PaletteColors
import com.fractalwrench.crazycats.image.Presenter
import com.fractalwrench.crazycats.injection.DefaultSchedulers
import io.reactivex.Observable

class CameraActivityPresenter(val schedulers: DefaultSchedulers, val repository: BitmapRepository) : Presenter<CameraView>() {

    fun onRateClicked() {
        contentView?.displayPlayStoreListing()
    }

    fun onGalleryClicked() {
        contentView?.launchGallery()
    }

    fun onCaptureClicked() {
        contentView?.captureImagePreview()
    }

    fun onBitmapCreated(jpeg: ByteArray?) {
        val observable = Observable.just(jpeg)
                .map { BitmapFactory.decodeByteArray(jpeg, 0, jpeg?.size!!) }
                .flatMap { repository.saveBitmap(it) }
                .map { it.recycle() }
                .flatMap { getPaletteObservable() }
                .compose { schedulers.apply(it) }

        compositeDisposable?.add(observable.subscribe({
            contentView?.updatePaletteColors(it)
        }, {
            it?.printStackTrace() // TODO
        }))
    }

    private fun getPaletteObservable(): Observable<PaletteColors> {
        val observable = repository.loadBitmap()
                .map {
                    val palette = Palette.from(it).generate()
                    it.recycle()
                    palette
                }
                .map(::PaletteColors)
                .compose { schedulers.apply(it) }
        return observable
    }

}
