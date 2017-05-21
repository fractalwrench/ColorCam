package com.fractalwrench.colorcam.inject

import com.fractalwrench.colorcam.ColorCamApp
import com.fractalwrench.colorcam.ui.CameraActivityComponent
import com.fractalwrench.colorcam.ui.CameraActivityModule
import com.fractalwrench.colorcam.ui.ColorDisplayActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class, SchedulerModule::class, DataModule::class))
interface AppComponent {

    fun inject(activity: ColorDisplayActivity)
    fun inject(app: ColorCamApp)
    fun plus(module: CameraActivityModule): CameraActivityComponent


}
