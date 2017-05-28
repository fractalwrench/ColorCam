package com.fractalwrench.colorcam.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.flurgle.camerakit.CameraListener
import com.fractalwrench.colorcam.ColorCamApp
import com.fractalwrench.colorcam.R
import com.fractalwrench.colorcam.image.PaletteColors
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class CameraActivity : AppCompatActivity(), CameraView {

    private val PHOTO_REQUEST_CODE = 9001

    private val cameraListener = ColorCameraListener()
    private var disposable: CompositeDisposable? = null

    @Inject lateinit var presenter: CameraActivityPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val app = application as ColorCamApp
        app.component.plus(CameraActivityModule()).inject(this)
    }

    override fun onStart() {
        super.onStart()
        disposable = CompositeDisposable()
        presenter.start(this)

        val d = disposable!!

        d.add(RxView.clicks(cameraButton)
                .map { presenter.onCaptureClicked() }
                .subscribe())

        d.add(RxView.clicks(galleryButton)
                .map { presenter.onGalleryClicked() }
                .subscribe())

        d.add(RxView.clicks(rateButton)
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
        camera.stop()
        camera.setCameraListener(null)
    }


    // CameraView


    override fun launchRate() {
        IntentUtils.launchPlayStoreListing(this)
    }

    override fun launchGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        val chooserIntent = Intent.createChooser(intent, getString(R.string.pick_photo))
        startActivityForResult(chooserIntent, PHOTO_REQUEST_CODE)
    }

    override fun capturePhoto() {
        camera.captureImage()
    }

    override fun displayCapturedImage(bmp: Bitmap, colors: PaletteColors) {
        static_image_preview.visibility = View.VISIBLE
        camera.visibility = View.GONE
        static_image_preview.setImageBitmap(bmp)
        camera.stop()
        displayPaletteColors(colors)
    }

    override fun displayCameraPreview() {
        static_image_preview.visibility = View.GONE
        camera.visibility = View.VISIBLE
        camera.start()
    }


    // Private


    private fun displayPaletteColors(colors: PaletteColors) {
        vibrantView.setBackgroundColor(colors.vibrant)
        vibrantLightView.setBackgroundColor(colors.vibrantLight)
        vibrantDarkView.setBackgroundColor(colors.vibrantDark)
        mutedView.setBackgroundColor(colors.muted)
        mutedLightView.setBackgroundColor(colors.mutedLight)
        mutedDarkView.setBackgroundColor(colors.mutedDark)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (PHOTO_REQUEST_CODE == requestCode && resultCode == Activity.RESULT_OK) {
            presenter.handleGalleryResult(data, this.applicationContext)
        }
    }

    private inner class ColorCameraListener : CameraListener() {
        override fun onPictureTaken(jpeg: ByteArray?) {
            super.onPictureTaken(jpeg)
            presenter.onCameraBitmapCaptured(jpeg)
        }

        override fun onCameraOpened() {
            super.onCameraOpened()
            presenter.onCameraStateChanged(true)
        }

        override fun onCameraClosed() {
            super.onCameraClosed()
            presenter.onCameraStateChanged(false)
        }
    }


}
