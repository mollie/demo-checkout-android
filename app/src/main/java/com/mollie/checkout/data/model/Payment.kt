package com.mollie.checkout.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.mollie.checkout.util.Formatting
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Payment(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("mollie_id")
    val mollieId: String? = null,
    @SerializedName("method")
    val method: String? = null,
    @SerializedName("issuer")
    val issuer: String? = null,
    @SerializedName("amount")
    val amount: Double,
    @SerializedName("description")
    val description: String,
    @SerializedName("url")
    val url: String? = null,
    @SerializedName("status")
    val status: PaymentStatus? = null,
    @SerializedName("created_at")
    val created: Date? = null,
    @SerializedName("updated_at")
    val updated: Date? = null,
): Parcelable, ListItemPayment {

    fun getFormattedPrice(): String? {
        return Formatting.getFormattedPrice(amount, withCurrency = true)
    }

    fun getFormattedCreated(): String? {
        return Formatting.getFormattedDate(created)
    }
}