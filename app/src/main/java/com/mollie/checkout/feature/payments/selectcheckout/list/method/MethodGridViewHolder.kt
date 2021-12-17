package com.mollie.checkout.feature.payments.selectcheckout.list.method

import com.bumptech.glide.Glide
import com.mollie.checkout.data.model.Issuer
import com.mollie.checkout.data.model.Method
import com.mollie.checkout.databinding.ItemMethodGridBinding
import com.mollie.checkout.feature.payments.selectcheckout.list.SelectCheckoutListener

class MethodGridViewHolder(
    val binding: ItemMethodGridBinding,
) : MethodBaseViewHolder(binding.root) {

    override fun render(
        method: Method,
        selected: Boolean,
        selectedIssuer: Issuer?,
        listener: SelectCheckoutListener,
    ) {
        binding.method = method
        binding.selected = selected
        binding.executePendingBindings()

        Glide
            .with(itemView)
            .load(method.image.size2x)
            .centerInside()
            .into(binding.icon)

        binding.root.setOnClickListener { listener.onMethodClicked(method) }
    }
}