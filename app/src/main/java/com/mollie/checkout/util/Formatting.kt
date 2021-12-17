package com.mollie.checkout.util

import android.text.format.DateUtils
import java.text.DateFormat
import java.text.DecimalFormat
import java.util.*

object Formatting {

    fun getFormattedDate(date: Date?): String? {
        date ?: return null
        return DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(date)
    }

    fun getFormattedDay(date: Date?): String? {
        date ?: return null
        return DateFormat.getDateInstance(DateFormat.SHORT).format(date)
    }

    fun getFormattedAgo(date: Date?): String? {
        date ?: return null
        return DateUtils.getRelativeTimeSpanString(date.time, Date().time, DateUtils.DAY_IN_MILLIS)
            .toString()
    }

    fun getFormattedPrice(amount: Double?, withCurrency: Boolean): String? {
        amount ?: return null
        val format = if (withCurrency) {
            "€ 0.00"
        } else {
            "0.00"
        }
        return DecimalFormat(format).format(amount)
    }
}