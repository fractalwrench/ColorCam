package com.fractalwrench.colorcam.inject

import com.fractalwrench.colorcam.CameraActivity
import com.fractalwrench.colorcam.ColorCamApp
import com.fractalwrench.colorcam.ColorDisplayActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class, SchedulerModule::class, DataModule::class))
interface AppComponent {

    fun inject(activity: CameraActivity)
    fun inject(activity: ColorDisplayActivity)
    fun inject(app: ColorCamApp)

}
