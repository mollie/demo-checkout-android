package com.mollie.checkout.util

import android.view.View

fun View.setVisible(visible: Boolean?) {
    visibility = if (visible == true) View.VISIBLE else View.GONE
}

fun View.setInvisible(invisible: Boolean?) {
    visibility = if (invisible == true) View.INVISIBLE else View.VISIBLE
}

fun View.setGone(gone: Boolean?) {
    visibility = if (gone == true) View.GONE else View.VISIBLE
}