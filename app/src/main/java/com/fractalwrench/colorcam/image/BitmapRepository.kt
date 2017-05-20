package com.fractalwrench.colorcam.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import io.reactivex.Observable
import java.io.File
import java.io.FileOutputStream

class BitmapRepository(context: Context) {

    private val bmpName = "data.jpg"
    private val photoDir = context.filesDir

    fun loadBitmap(): Observable<Bitmap> {
        return Observable.just(getFileName(bmpName))
                .map { BitmapFactory.decodeFile(it.absolutePath) }
    }

    fun saveBitmap(bitmap: Bitmap): Observable<Bitmap> {
        return Observable.just(bitmap)
                .doOnNext { bmp ->
                    val file = getFileName(bmpName)
                    FileOutputStream(file).use {
                        bmp.compress(Bitmap.CompressFormat.JPEG, 100, it)
                        it.flush()
                        it.close()
                    }
                }
    }

    private fun getFileName(name: String) = File(photoDir, name)

}
