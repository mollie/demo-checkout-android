package com.mollie.checkout.feature.payments.selectcheckout.list.issuer

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.mollie.checkout.data.model.Issuer
import com.mollie.checkout.feature.payments.selectcheckout.list.SelectCheckoutListener

abstract class IssuerBaseViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    abstract fun render(
        issuer: Issuer,
        selected: Boolean,
        listener: SelectCheckoutListener?
    )
}