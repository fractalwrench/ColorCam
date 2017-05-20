package com.fractalwrench.crazycats.injection

import io.reactivex.Observable
import io.reactivex.Scheduler

/**
 * Defines io/ui schedulers (which are swapped out to facilitate testing)
 */
class DefaultSchedulers(private val io: Scheduler, private val ui: Scheduler) {

    fun io(): Scheduler {
        return io
    }

    fun ui(): Scheduler {
        return ui
    }

    /**
     * Should be called using [Observable.compose] to set the default
     * schedulers for an observable.

     * @param observable the observable
     * *
     * @param <T>        type
     * *
     * @return the observable with newly set schedulers
    </T> */
    fun <T> apply(observable: Observable<T>): Observable<T> {
        return observable.subscribeOn(io)
                .observeOn(ui)
    }

}
