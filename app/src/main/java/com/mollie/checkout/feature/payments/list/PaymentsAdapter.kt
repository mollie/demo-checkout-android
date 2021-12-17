package com.mollie.checkout.feature.payments.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mollie.checkout.data.model.ListItemPayment
import com.mollie.checkout.data.model.Payment
import com.mollie.checkout.data.model.Section
import com.mollie.checkout.databinding.ItemPaymentBinding
import com.mollie.checkout.databinding.ItemSectionBinding

class PaymentsAdapter(
    private val listener: Listener,
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface Listener {
        fun onPaymentClicked(payment: Payment)
    }

    private enum class Type(
        val index: Int,
    ) {
        SECTION(1),
        PAYMENT(2),
    }

    private val items = mutableListOf<ListItemPayment>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            Type.SECTION.index -> {
                val binding = ItemSectionBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return SectionViewHolder(binding)
            }
            Type.PAYMENT.index -> {
                val binding = ItemPaymentBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return PaymentViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Unsupported view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        when {
            holder is SectionViewHolder && item is Section -> holder.render(item)
            holder is PaymentViewHolder && item is Payment -> holder.render(item, listener)
            else -> throw IllegalArgumentException("Unsupported bind for item $item")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (val item = items[position]) {
            is Section -> Type.SECTION.index
            is Payment -> Type.PAYMENT.index
            else -> throw IllegalArgumentException("Unsupported type for item $item")
        }
    }

    override fun getItemCount() = items.size

    fun show(items: List<ListItemPayment>?) {
        this.items.clear()
        items?.let {
            this.items.addAll(items)
        }
        this.notifyDataSetChanged()
    }
}