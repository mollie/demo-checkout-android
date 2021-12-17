package com.mollie.checkout.feature.payments.selectcheckout.list.method

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.mollie.checkout.data.model.Issuer
import com.mollie.checkout.data.model.Method
import com.mollie.checkout.feature.payments.selectcheckout.list.SelectCheckoutListener

abstract class MethodBaseViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    abstract fun render(
        method: Method,
        selected: Boolean,
        selectedIssuer: Issuer?,
        listener: SelectCheckoutListener,
    )
}