package com.mollie.checkout.data.model

import com.google.gson.annotations.SerializedName

data class Image(
    @SerializedName("size_1x")
    val size1x: String,
    @SerializedName("size_2x")
    val size2x: String,
    @SerializedName("svg")
    val svg: String,
)
