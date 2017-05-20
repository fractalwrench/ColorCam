package com.fractalwrench.colorcam

import android.app.Application
import android.os.StrictMode
import com.fractalwrench.colorcam.inject.AppComponent
import com.fractalwrench.colorcam.inject.AppModule
import com.fractalwrench.colorcam.inject.DaggerAppComponent
import com.squareup.leakcanary.LeakCanary

class ColorCamApp : Application() {

    override fun onCreate() {
        super.onCreate()
        setupStrictModePolicy()
        initialiseLeakCanary()
        component.inject(this)
    }

    private fun setupStrictModePolicy() { // log out any bad practices
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().detectAll()
                    .penaltyLog()
                    .build())

            StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder().detectAll()
                    .penaltyLog()
                    .build())
        }
    }

    private fun initialiseLeakCanary() { // catch memory leaks
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }
        LeakCanary.install(this)
    }

    val component: AppComponent by lazy {
        DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .build()
    }

}