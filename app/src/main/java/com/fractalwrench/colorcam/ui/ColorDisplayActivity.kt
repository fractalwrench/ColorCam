package com.fractalwrench.colorcam.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.graphics.Palette
import com.fractalwrench.colorcam.ColorCamApp
import com.fractalwrench.colorcam.R
import com.fractalwrench.colorcam.image.BitmapRepository
import com.fractalwrench.colorcam.image.PaletteColors
import com.fractalwrench.crazycats.injection.DefaultSchedulers
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_colors.*


class ColorDisplayActivity : AppCompatActivity() {

    private var disposable: CompositeDisposable? = null

    @javax.inject.Inject lateinit var schedulers: DefaultSchedulers
    @javax.inject.Inject lateinit var repository: BitmapRepository

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
        fun launch(context: Context): Intent {
            val intent = Intent(context, ColorDisplayActivity::class.java)
            return intent
        }
    }

}
