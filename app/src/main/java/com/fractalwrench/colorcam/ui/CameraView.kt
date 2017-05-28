package com.fractalwrench.colorcam.ui

import android.graphics.Bitmap
import com.fractalwrench.colorcam.image.PaletteColors

interface CameraView {

    fun launchRate()
    fun launchGallery()
    fun capturePhoto()
    fun displayCapturedImage(bmp: Bitmap, colors: PaletteColors)
    fun displayCameraPreview()

}

