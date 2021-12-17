package com.mollie.checkout.feature.base

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {

    private val loading = MutableLiveData<Boolean>()
    private val error = MutableLiveData<Any?>()

    fun isLoading(): LiveData<Boolean> = loading
    fun getError(): LiveData<Any?> = error

    protected fun setLoading(loading: Boolean) {
        this.loading.value = loading
    }

    protected fun setError(error: String) {
        this.error.value = error
    }

    protected fun setError(@StringRes error: Int) {
        this.error.value = error
    }

    protected fun clearError() {
        this.error.value = null
    }
}