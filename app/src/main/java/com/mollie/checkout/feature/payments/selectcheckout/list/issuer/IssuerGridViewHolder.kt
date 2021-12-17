package com.mollie.checkout.feature.payments.selectcheckout.list.issuer

import com.bumptech.glide.Glide
import com.mollie.checkout.data.model.Issuer
import com.mollie.checkout.databinding.ItemIssuerGridBinding
import com.mollie.checkout.feature.payments.selectcheckout.list.SelectCheckoutListener

class IssuerGridViewHolder(
    val binding: ItemIssuerGridBinding,
) : IssuerBaseViewHolder(binding.root) {

    override fun render(issuer: Issuer, selected: Boolean, listener: SelectCheckoutListener?) {
        binding.issuer = issuer
        binding.selected = selected
        binding.executePendingBindings()

        Glide
            .with(itemView)
            .load(issuer.image.size2x)
            .centerInside()
            .into(binding.icon)

        binding.root.setOnClickListener {
            listener?.onIssuerClicked(issuer)
        }
    }
}