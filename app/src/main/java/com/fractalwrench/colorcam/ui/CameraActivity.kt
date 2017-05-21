package com.fractalwrench.colorcam.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.flurgle.camerakit.CameraListener
import com.fractalwrench.colorcam.ColorCamApp
import com.fractalwrench.colorcam.R
import com.fractalwrench.colorcam.image.PaletteColors
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class CameraActivity : AppCompatActivity(), CameraView {

    private val cameraListener = ColorCameraListener()
    private var disposable: CompositeDisposable? = null

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

        disposable?.add(RxView.clicks(cameraButton)
                .map { presenter.onCaptureClicked() }
                .subscribe())

        disposable?.add(RxView.clicks(galleryButton)
                .map { presenter.onGalleryClicked() }
                .subscribe())

        disposable?.add(RxView.clicks(rateButton)
                .map { presenter.onRateClicked() }
                .subscribe())
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

    private inner class ColorCameraListener : CameraListener() {
        override fun onPictureTaken(jpeg: ByteArray?) {
            super.onPictureTaken(jpeg)
            presenter.onBitmapCreated(jpeg)
        }

        override fun onCameraOpened() {
            super.onCameraOpened()
        }
    }


    // CameraView


    override fun displayPlayStoreListing() {
        IntentUtils.launchPlayStoreListing(this)
    }

    override fun launchGallery() {
        TODO("not implemented") // TODO launch gallery
    }

    override fun captureImagePreview() {
        camera.captureImage() // TODO
    }

    override fun updatePaletteColors(colors: PaletteColors) {
        vibrantView.setBackgroundColor(colors.vibrant)
        vibrantLightView.setBackgroundColor(colors.vibrantLight)
        vibrantDarkView.setBackgroundColor(colors.vibrantDark)
        mutedView.setBackgroundColor(colors.muted)
        mutedLightView.setBackgroundColor(colors.mutedLight)
        mutedDarkView.setBackgroundColor(colors.mutedDark)
    }
}
