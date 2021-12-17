package com.mollie.checkout.util

import android.view.View
import androidx.annotation.ColorRes
import androidx.databinding.BindingAdapter

@BindingAdapter("visible")
fun bindVisible(view: View, value: Boolean?) {
    view.setVisible(value)
}

@BindingAdapter("invisible")
fun bindInvisible(view: View, value: Boolean?) {
    view.setInvisible(value)
}

@BindingAdapter("gone")
fun bindGone(view: View, value: Boolean?) {
    view.setGone(value)
}

@BindingAdapter("backgroundTint")
fun setTint(view: View, @ColorRes tint: Int?) {
    view.backgroundTintList = tint?.asColorStateList(view.context)
}
