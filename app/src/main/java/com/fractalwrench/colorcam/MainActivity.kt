package com.fractalwrench.colorcam

import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.flurgle.camerakit.CameraListener
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity() {

    private val cameraListener = object : CameraListener() {
        override fun onPictureTaken(jpeg: ByteArray?) {
            super.onPictureTaken(jpeg)
            val result = BitmapFactory.decodeByteArray(jpeg, 0, jpeg?.size!!)
        }
    }

    private var disposable: CompositeDisposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        disposable = CompositeDisposable()
        camera.start()
        camera.setCameraListener(cameraListener)

        disposable?.add(RxView.clicks(cameraButton)
                .subscribe({ camera.captureImage() }))
    }

    override fun onPause() {
        super.onPause()
        camera.stop()
        camera.setCameraListener(null)
        disposable?.dispose()
    }

}
