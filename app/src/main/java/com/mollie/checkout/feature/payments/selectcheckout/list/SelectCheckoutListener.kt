package com.mollie.checkout.feature.payments.selectcheckout.list

import com.mollie.checkout.data.model.Issuer
import com.mollie.checkout.data.model.Method

interface SelectCheckoutListener {
    fun onMethodClicked(method: Method)
    fun onIssuerClicked(issuer: Issuer)
}