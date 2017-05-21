package com.fractalwrench.crazycats.injection

import javax.inject.Scope

/**
 * This Scope identifies that the dependencies should only be retained for the lifecycle of an
 * activity
 */
@Scope
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class ActivityScope
