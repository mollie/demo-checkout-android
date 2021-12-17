package com.mollie.checkout.feature.inappbrowser

import com.mollie.checkout.data.PaymentsRepository
import com.mollie.checkout.data.model.Payment
import com.mollie.checkout.feature.base.BaseViewModel

class InAppBrowserViewModel : BaseViewModel() {

    init {
        setLoading(true)
    }

    suspend fun getLatestPayment(id: Int): Payment? {
        setLoading(true)
        val response = PaymentsRepository.retrievePayment(id)
        if (response.isSuccess()) {
            // Nothing to do
        } else {
            setError(response.getError())
        }
        setLoading(false)
        return response.getData()?.data
    }

    fun onWebViewStartedLoading() {
        setLoading(true)
    }

    fun onWebViewFinishedLoading() {
        setLoading(false)
    }
}