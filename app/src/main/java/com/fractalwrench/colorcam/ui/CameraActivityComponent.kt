package com.fractalwrench.colorcam.ui


import com.fractalwrench.crazycats.injection.ActivityScope

import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = arrayOf(CameraActivityModule::class))
interface CameraActivityComponent {

    fun inject(activity: CameraActivity)

}
