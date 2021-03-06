package com.fractalwrench.colorcam.inject

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Module for providing the singleton application context
 */
@Singleton
@Module
class AppModule(private val context: Context) {

    @Provides
    @Singleton fun providesApplicationContext() = context

}
