package com.fractalwrench.colorcam.ui

import com.fractalwrench.colorcam.image.PaletteColors

interface CameraView {

    fun displayPlayStoreListing()
    fun launchGallery()
    fun captureImagePreview()
    fun updatePaletteColors(colors: PaletteColors)

}

