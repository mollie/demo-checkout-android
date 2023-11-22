package com.mollie.checkout.data.model

enum class SelectCheckoutLayoutStyle(
    val index: Int,
) {
    LIST(0),
    GRID(1);

    companion object {
        fun from(index: Int?): SelectCheckoutLayoutStyle? {
            return entries.firstOrNull { it.index == index }
        }
    }
}