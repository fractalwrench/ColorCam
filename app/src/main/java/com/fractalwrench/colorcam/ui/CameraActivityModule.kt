package com.fractalwrench.colorcam.ui

import com.fractalwrench.colorcam.image.BitmapRepository
import com.fractalwrench.crazycats.injection.ActivityScope
import com.fractalwrench.crazycats.injection.DefaultSchedulers
import dagger.Module
import dagger.Provides

@Module
@ActivityScope
class CameraActivityModule {

    @Provides fun providesPresenter(schedulers: DefaultSchedulers, repository: BitmapRepository)
            = CameraActivityPresenter(schedulers, repository)

}
