package com.fractalwrench.colorcam.inject

import com.fractalwrench.colorcam.ColorCamApp
import com.fractalwrench.colorcam.ColorDisplayActivity
import com.fractalwrench.colorcam.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class, SchedulerModule::class))
interface AppComponent {

    fun inject(activity: MainActivity)
    fun inject(activity: ColorDisplayActivity)
    fun inject(app: ColorCamApp)

}
