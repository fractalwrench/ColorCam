package com.fractalwrench.colorcam.inject


import android.content.Context
import com.fractalwrench.colorcam.image.BitmapRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Module
class DataModule {

    @Provides
    @Singleton
    fun providesRepo(context: Context) = BitmapRepository(context)

}
