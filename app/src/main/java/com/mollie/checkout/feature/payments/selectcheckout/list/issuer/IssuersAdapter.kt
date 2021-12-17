package com.mollie.checkout.feature.payments.selectcheckout.list.issuer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mollie.checkout.data.model.Issuer
import com.mollie.checkout.data.model.SelectCheckoutLayoutStyle
import com.mollie.checkout.databinding.ItemIssuerGridBinding
import com.mollie.checkout.databinding.ItemIssuerListBinding
import com.mollie.checkout.feature.payments.selectcheckout.list.SelectCheckoutListener

class IssuersAdapter(
    private val style: SelectCheckoutLayoutStyle
) : RecyclerView.Adapter<IssuerBaseViewHolder>() {

    private val issuers = mutableListOf<Issuer>()
    private var selectedIssuer: Issuer? = null
    private var listener: SelectCheckoutListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IssuerBaseViewHolder {
        when (style.index) {
            SelectCheckoutLayoutStyle.LIST.index -> {
                val binding = ItemIssuerListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return IssuerListViewHolder(binding)
            }
            SelectCheckoutLayoutStyle.GRID.index -> {
                val binding = ItemIssuerGridBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return IssuerGridViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Unsupported for style: $style")
        }
    }

    override fun onBindViewHolder(holder: IssuerBaseViewHolder, position: Int) {
        val issuer = issuers[position]
        holder.render(issuer, issuer.id == selectedIssuer?.id, listener)
    }

    override fun getItemCount() = issuers.size

    override fun getItemViewType(position: Int) = style.index

    fun show(issuers: List<Issuer>?, selectedIssuer: Issuer?, listener: SelectCheckoutListener?) {
        this.issuers.clear()
        issuers?.let {
            this.issuers.addAll(issuers)
        }
        this.selectedIssuer = selectedIssuer
        this.listener = listener
        this.notifyDataSetChanged()
    }
}