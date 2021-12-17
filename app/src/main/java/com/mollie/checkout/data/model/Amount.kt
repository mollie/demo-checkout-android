package com.mollie.checkout.data.model

import com.google.gson.annotations.SerializedName

data class Amount(
    @SerializedName("value")
    val value: Double,
    @SerializedName("currency")
    val currency: String,
)
