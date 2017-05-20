package com.fractalwrench.colorcam

import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.flurgle.camerakit.CameraListener
import com.fractalwrench.colorcam.image.BitmapRepository
import com.fractalwrench.crazycats.injection.DefaultSchedulers
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class CameraActivity : AppCompatActivity() {

    private val cameraListener = ColorCameraListener()
    private var disposable: CompositeDisposable? = null

    @Inject lateinit var schedulers: DefaultSchedulers
    @Inject lateinit var repository: BitmapRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val app = application as ColorCamApp
        app.component.inject(this)
    }

    override fun onResume() {
        super.onResume()
        disposable = CompositeDisposable()
        camera.start()
        camera.setCameraListener(cameraListener)

        val observable = RxView.clicks(cameraButton)
        disposable?.add(observable.subscribe({
            camera.captureImage()
            // TODO display progress here!
        }))
    }

    override fun onPause() {
        super.onPause()
        camera.stop()
        camera.setCameraListener(null)
        disposable?.dispose()
    }

    private fun onBitmapCreated(jpeg: ByteArray?) {
        val observable = Observable.just(jpeg)
                .map { BitmapFactory.decodeByteArray(jpeg, 0, jpeg?.size!!) }
                .map {
                    repository.saveBitmap(it)
                    it.recycle()
                }
                .compose { schedulers.apply(it) }

        disposable?.add(observable.subscribe({
            startActivity(ColorDisplayActivity.launch(this))
        }) {
            it?.printStackTrace() // TODO
        })
    }

    private inner class ColorCameraListener : CameraListener() {
        override fun onPictureTaken(jpeg: ByteArray?) {
            super.onPictureTaken(jpeg)
            onBitmapCreated(jpeg)
        }
    }

}
