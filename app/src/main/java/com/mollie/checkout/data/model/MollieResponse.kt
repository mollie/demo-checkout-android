package com.mollie.checkout.data.model

import com.google.gson.annotations.SerializedName

data class MollieResponse<T>(
    @SerializedName("data")
    val data: T
)