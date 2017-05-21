package com.fractalwrench.crazycats.image

import io.reactivex.disposables.CompositeDisposable

/**
 * Presenters are responsible for responding to user interactions from a View, by
 * communicating with the model layer to determine what the view should display.
 */
abstract class Presenter<T> {

    var contentView: T? = null
    protected var compositeDisposable: CompositeDisposable? = null

    private var isPresenting = false

    /**
     * Notify the presenter that it should start telling its View how to display itself.
     */
    open fun start(contentView: T) {
        compositeDisposable = CompositeDisposable()

        if (isPresenting) {
            throw IllegalStateException("Already presenting, please call stop() first")
        }

        this.contentView = contentView
        isPresenting = true
    }

    /**
     * Notify the presenter that it should stop telling its View how to display itself.
     */
    open fun stop() {
        if (!isPresenting) {
            throw IllegalStateException("Already not presenting, please call start() first")
        }
        this.contentView = null
        this.isPresenting = false

        if (compositeDisposable!!.isDisposed) {
            throw IllegalStateException("Attempted to dispose an already disposed composite")
        } else {
            compositeDisposable?.dispose()
        }
    }

}
