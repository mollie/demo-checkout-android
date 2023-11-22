package com.mollie.checkout.data.model

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.google.gson.annotations.SerializedName
import com.mollie.checkout.R

@Suppress("unused")
enum class PaymentStatus(
    @StringRes val label: Int,
    @ColorRes val color: Int,
    val isCompleted: Boolean,
) {
    @SerializedName("open")
    OPEN(
        R.string.status_open,
        R.color.status_yellow,
        false,
    ),

    @SerializedName("canceled")
    CANCELED(
        R.string.status_canceled,
        R.color.status_grey,
        true,
    ),

    @SerializedName("pending")
    PENDING(
        R.string.status_pending,
        R.color.status_orange,
        false,
    ),

    @SerializedName("expired")
    EXPIRED(
        R.string.status_expired,
        R.color.status_grey,
        true,
    ),

    @SerializedName("failed")
    FAILED(
        R.string.status_failed,
        R.color.status_red,
        true,
    ),

    @SerializedName("paid")
    PAID(
        R.string.status_paid,
        R.color.status_green,
        true,
    ),
}