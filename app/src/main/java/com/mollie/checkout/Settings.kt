package com.mollie.checkout

import com.mollie.checkout.data.model.PaymentFlow

object Settings {

    object Network {
        // TODO: Change the base url
        const val BASE_URL = "http://localhost:8000/api/"
    }

    object Navigation {

        const val SELECT_PAYMENT_METHOD = false

        val PAYMENT_FLOW = PaymentFlow.CHOOSE
    }
}