package com.mollie.checkout.data.model

import com.google.gson.annotations.SerializedName

data class Issuer(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("image")
    val image: Image,
)
