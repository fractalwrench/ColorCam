package com.fractalwrench.crazycats.image

import io.reactivex.disposables.CompositeDisposable

/**
 * Presenters are responsible for responding to user interactions from a View, by
 * communicating with the model layer to determine what the view should display.
 */
abstract class Presenter<T> {

    var view: T? = null
    protected var compositeDisposable: CompositeDisposable? = null

    private var isPresenting = false

    /**
     * Notify the presenter that it should start telling its View how to display itself.
     */
    open fun start(view: T) {
        compositeDisposable = CompositeDisposable()

        if (isPresenting) {
            throw IllegalStateException("Already presenting, please call stop() first")
        }

        this.view = view
        isPresenting = true
    }

    /**
     * Notify the presenter that it should stop telling its View how to display itself.
     */
    open fun stop() {
        if (!isPresenting) {
            throw IllegalStateException("Already not presenting, please call start() first")
        }
        this.view = null
        this.isPresenting = false

        if (compositeDisposable!!.isDisposed) {
            throw IllegalStateException("Attempted to dispose an already disposed composite")
        } else {
            compositeDisposable?.dispose()
        }
    }

}
