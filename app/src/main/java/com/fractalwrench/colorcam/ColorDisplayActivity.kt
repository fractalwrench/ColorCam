package com.fractalwrench.colorcam

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.graphics.Palette
import com.fractalwrench.colorcam.image.BitmapRepository
import com.fractalwrench.colorcam.image.PaletteColors
import com.fractalwrench.crazycats.injection.DefaultSchedulers
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_colors.*
import javax.inject.Inject


class ColorDisplayActivity : AppCompatActivity() {

    private var disposable: CompositeDisposable? = null

    @Inject lateinit var schedulers: DefaultSchedulers
    @Inject lateinit var repository: BitmapRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_colors)
        val app = application as ColorCamApp
        app.component.inject(this)
    }

    override fun onResume() {
        super.onResume()
        disposable = CompositeDisposable()
        disposable?.add(getPaletteObservable().subscribe({
            palette ->
            display_bg.setBackgroundColor(palette.vibrant)
            // TODO use!
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
