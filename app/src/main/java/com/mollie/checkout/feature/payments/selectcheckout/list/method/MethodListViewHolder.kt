package com.mollie.checkout.feature.payments.selectcheckout.list.method

import com.bumptech.glide.Glide
import com.mollie.checkout.R
import com.mollie.checkout.data.model.Issuer
import com.mollie.checkout.data.model.Method
import com.mollie.checkout.data.model.SelectCheckoutLayoutStyle
import com.mollie.checkout.databinding.ItemMethodListBinding
import com.mollie.checkout.feature.payments.selectcheckout.list.SelectCheckoutListener
import com.mollie.checkout.feature.payments.selectcheckout.list.issuer.IssuersAdapter

class MethodListViewHolder(
    val binding: ItemMethodListBinding,
) : MethodBaseViewHolder(binding.root) {

    private val adapter = IssuersAdapter(SelectCheckoutLayoutStyle.LIST)

    init {
        binding.recyclerView.adapter = adapter
    }

    override fun render(
        method: Method,
        selected: Boolean,
        selectedIssuer: Issuer?,
        listener: SelectCheckoutListener,
    ) {
        binding.method = method
        binding.selected = selected
        binding.actionIcon.setImageResource(
            if (method.issuers.isNullOrEmpty()) {
                R.drawable.ic_check_circle
            } else {
                R.drawable.ic_expand_more
            }
        )
        adapter.show(method.issuers, selectedIssuer, listener)
        binding.executePendingBindings()

        Glide
            .with(itemView)
            .load(method.image.size2x)
            .centerInside()
            .into(binding.icon)

        binding.methodLayout.setOnClickListener { listener.onMethodClicked(method) }
    }
}