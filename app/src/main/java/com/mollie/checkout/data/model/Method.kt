package com.mollie.checkout.data.model

import com.google.gson.annotations.SerializedName

data class Method(
    @SerializedName("id")
    val id: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("minimum_amount")
    val minimumAmount: Amount,
    @SerializedName("maximum_amount")
    val maximumAmount: Amount,
    @SerializedName("image")
    val image: Image,
    @SerializedName("issuers")
    val issuers: List<Issuer>?
)
