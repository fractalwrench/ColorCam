package com.fractalwrench.colorcam.ui

import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.graphics.Palette
import com.flurgle.camerakit.CameraListener
import com.fractalwrench.colorcam.ColorCamApp
import com.fractalwrench.colorcam.R
import com.fractalwrench.colorcam.image.BitmapRepository
import com.fractalwrench.colorcam.image.PaletteColors
import com.fractalwrench.crazycats.injection.DefaultSchedulers
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class CameraActivity : AppCompatActivity(), CameraView {

    private val cameraListener = ColorCameraListener()
    private var disposable: CompositeDisposable? = null

    @Inject lateinit var schedulers: DefaultSchedulers
    @Inject lateinit var repository: BitmapRepository
    @Inject lateinit var presenter: CameraActivityPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val app = application as ColorCamApp
        app.component.plus(CameraActivityModule()).inject(this)
    }

    private var cameraDisposable: Disposable? = null

    override fun onStart() {
        super.onStart()
        disposable = CompositeDisposable()
        presenter.start(this)


        // TODO abstract logic out?

        disposable?.add(RxView.clicks(cameraButton).subscribe({
            camera.captureImage()
            // TODO display progress here!
        }))


        disposable?.add(RxView.clicks(galleryButton).subscribe {
            // TODO launch gallery
        })

        disposable?.add(RxView.clicks(rateButton).subscribe {
            IntentUtils.launchPlayStoreListing(this)
        })
    }


    override fun onStop() {
        super.onStop()
        presenter.stop()
        disposable?.dispose()
    }

    override fun onResume() {
        super.onResume()
        camera.start()
        camera.setCameraListener(cameraListener)
    }

    override fun onPause() {
        super.onPause()
        cameraDisposable?.dispose()
        camera.stop()
        camera.setCameraListener(null)
    }

    private fun onBitmapCreated(jpeg: ByteArray?) {
        val observable = Observable.just(jpeg)
                .map { BitmapFactory.decodeByteArray(jpeg, 0, jpeg?.size!!) }
                .flatMap { repository.saveBitmap(it) }
                .map { it.recycle() }
                .flatMap { getPaletteObservable() }
                .compose { schedulers.apply(it) }

        disposable?.add(observable.subscribe({
            palette ->
            color_view.setBackgroundColor(palette.vibrant) // TODO use!
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

    private inner class ColorCameraListener : CameraListener() {
        override fun onPictureTaken(jpeg: ByteArray?) {
            super.onPictureTaken(jpeg)
            onBitmapCreated(jpeg)
        }
    }

}
