package com.fractalwrench.colorcam.ui

import com.fractalwrench.crazycats.injection.ActivityScope
import dagger.Module
import dagger.Provides

@Module
@ActivityScope
class CameraActivityModule {

    @Provides fun providesPresenter() = CameraActivityPresenter()

}
