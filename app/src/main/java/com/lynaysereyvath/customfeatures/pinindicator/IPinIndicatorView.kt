package com.lynaysereyvath.customfeatures.pinindicator

interface IPinIndicatorView {

    fun setError()
    fun onFinished()
    fun enableLoading(enable: Boolean): PinIndicatorView
    fun onFinishedLoading()
    fun addOnFinishedListener(listener: () -> Unit): PinIndicatorView
    fun addOnFinishedLoadingListener(listener: () -> Unit): PinIndicatorView
}