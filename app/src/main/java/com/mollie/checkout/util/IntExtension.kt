package com.mollie.checkout.util

import android.content.Context
import androidx.core.content.ContextCompat

fun Int.asColorStateList(context: Context) = ContextCompat.getColorStateList(context, this)
