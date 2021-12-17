package com.mollie.checkout.feature.payments.list

import androidx.recyclerview.widget.RecyclerView
import com.mollie.checkout.data.model.Payment
import com.mollie.checkout.databinding.ItemPaymentBinding

class PaymentViewHolder(
    private val binding: ItemPaymentBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun render(payment: Payment, listener: PaymentsAdapter.Listener) {
        binding.payment = payment
        binding.executePendingBindings()

        binding.root.setOnClickListener {
            listener.onPaymentClicked(payment)
        }
    }
}