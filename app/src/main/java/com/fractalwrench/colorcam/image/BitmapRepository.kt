package com.fractalwrench.colorcam.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import io.reactivex.Observable
import java.io.File
import java.io.FileOutputStream

class BitmapRepository(context: Context) {

    private val filename = "data.jpg"
    private val photoDir = context.filesDir

    fun loadBitmap(): Observable<Bitmap> {
        return Observable.just(filename)
                .map { File(photoDir, it) }
                .map { loadFromFile(it) }
    }

    fun saveBitmap(bitmap: Bitmap): Observable<Boolean> {
        return Observable.just(bitmap)
                .doOnNext {
                    saveToFile(bitmap)
                    bitmap.recycle()
                }
                .map { true }
    }

    private fun saveToFile(bitmap: Bitmap) {
        val file = File(photoDir, filename)
        FileOutputStream(file).use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            it.flush()
        }
    }

    private fun loadFromFile(file: File): Bitmap {
        return BitmapFactory.decodeFile(file.path)
    }

}
