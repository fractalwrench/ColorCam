package com.fractalwrench.colorcam.inject


import com.fractalwrench.crazycats.injection.DefaultSchedulers
import dagger.Module
import dagger.Provides
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Singleton

/**
 * Defines the default scheduler threads for Observables. These can be swapped out when testing.
 */
@Singleton
@Module
class SchedulerModule {

    @Provides
    @Singleton
    internal fun providesAppSchedulers(): DefaultSchedulers {
        return DefaultSchedulers(Schedulers.io(),
                AndroidSchedulers.mainThread())
    }

}
