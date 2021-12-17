package com.mollie.checkout.feature.payments.create

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mollie.checkout.R
import com.mollie.checkout.data.PaymentsRepository
import com.mollie.checkout.data.model.Payment
import com.mollie.checkout.feature.base.BaseViewModel

class CreatePaymentViewModel : BaseViewModel() {
    companion object {

        private val TEST_PRODUCTS = listOf(
            Pair("Walkman", 19.99),
            Pair("Goldfish", 2.99),
            Pair("Back to the future DVD", 6.00),
            Pair("Running shoes", 49.00),
            Pair("Haircut for men - appointment", 29.95),
            Pair("Electric Vehicle", 49_999.00),
            Pair("Comic book", 7.50),
            Pair("Game console", 495.00),
            Pair("Drone", 900.00),
            Pair("Encyclopedia", 28.99),
            Pair("Robot arm", 1_450.00),
            Pair("Virtual reality headset", 599.00),
            Pair("Smartwatch", 379.95),
            Pair("Spencer ", 28.75),
        )

        // For testing in production
//        private val TEST_PRODUCTS = listOf(
//            Pair("TEST Webuildapps - Mollie example", 0.01),
//        )
    }

    private val initialProduct = MutableLiveData<Pair<String, Double>?>()

    fun getInitialProduct(): LiveData<Pair<String, Double>?> = initialProduct

    fun createPayment(amount: Double?, description: String?): Payment? {
        if (isLoading().value == true) {
            Log.d("${this.javaClass}", "Already saving, ignoring")
            return null
        }

        if (amount == null || amount == 0.0) {
            setError(R.string.create_payment_error_missing_amount)
            return null
        }

        if (description.isNullOrBlank()) {
            setError(R.string.create_payment_error_missing_description)
            return null
        }

        return Payment(
            amount = amount,
            description = description
        )
    }

    fun load(payment: Payment?) {
        if (payment == null) {
            initialProduct.value = TEST_PRODUCTS.random()
        } else {
            initialProduct.value = Pair(payment.description, payment.amount)
        }
    }

    suspend fun submit(payment: Payment): Payment? {
        setLoading(true)
        val response = PaymentsRepository.createPayment(
            payment.amount,
            payment.description,
        )
        if (response.isSuccess()) {
            // Nothing to do
        } else {
            setError(response.getError())
        }
        setLoading(false)
        return response.getData()?.data
    }
}