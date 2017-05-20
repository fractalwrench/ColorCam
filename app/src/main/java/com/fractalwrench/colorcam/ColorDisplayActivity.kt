package com.fractalwrench.colorcam

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.fractalwrench.crazycats.injection.DefaultSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


class ColorDisplayActivity : AppCompatActivity() {

    private var disposable: CompositeDisposable? = null

    @Inject lateinit var schedulers: DefaultSchedulers

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_colors)
        val app = application as ColorCamApp
        app.component.inject(this)
    }

    override fun onResume() {
        super.onResume()
        disposable = CompositeDisposable()
    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }

    companion object {
        fun launch(context: Context) : Intent {
            val intent = Intent(context, ColorDisplayActivity::class.java)
            return intent
        }
    }

}
