package com.mollie.checkout.data.model

import com.mollie.checkout.util.Formatting
import java.util.*

data class Section(
    val date: Date?,
): ListItemPayment {
    fun getFormattedDate(): String? {
        return Formatting.getFormattedAgo(date)
    }
}
