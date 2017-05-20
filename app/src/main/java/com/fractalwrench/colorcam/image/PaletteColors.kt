package com.fractalwrench.colorcam.image

import android.graphics.Color
import android.support.v7.graphics.Palette

class PaletteColors(palette: Palette) {

    private val default = Color.WHITE

    val vibrant = palette.getVibrantColor(default)
    val vibrantLight = palette.getLightVibrantColor(default)
    val vibrantDark = palette.getDarkVibrantColor(default)
    val muted = palette.getMutedColor(default)
    val mutedLight = palette.getLightMutedColor(default)
    val mutedDark = palette.getDarkMutedColor(default)

}
