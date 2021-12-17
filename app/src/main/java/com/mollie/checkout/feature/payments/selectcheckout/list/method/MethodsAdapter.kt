package com.mollie.checkout.feature.payments.selectcheckout.list.method

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mollie.checkout.data.model.Issuer
import com.mollie.checkout.data.model.Method
import com.mollie.checkout.data.model.SelectCheckoutLayoutStyle
import com.mollie.checkout.databinding.ItemMethodGridBinding
import com.mollie.checkout.databinding.ItemMethodListBinding
import com.mollie.checkout.feature.payments.selectcheckout.list.SelectCheckoutListener
import java.lang.IllegalArgumentException

class MethodsAdapter(
    private val listener: SelectCheckoutListener
) : RecyclerView.Adapter<MethodBaseViewHolder>() {

    private val methods = mutableListOf<Method>()
    private var selectedMethod: Method? = null
    private var selectedIssuer: Issuer? = null
    private var style = SelectCheckoutLayoutStyle.LIST

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MethodBaseViewHolder {
        when (style.index) {
            SelectCheckoutLayoutStyle.LIST.index -> {
                val binding = ItemMethodListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return MethodListViewHolder(binding)
            }
            SelectCheckoutLayoutStyle.GRID.index -> {
                val binding = ItemMethodGridBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return MethodGridViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Unsupported for style: $style")
        }
    }

    override fun onBindViewHolder(holder: MethodBaseViewHolder, position: Int) {
        val method = methods[position]
        holder.render(method, method.id == selectedMethod?.id, selectedIssuer, listener)
    }

    override fun getItemCount() = methods.size

    override fun getItemViewType(position: Int) = style.index

    fun show(methods: List<Method>?, style: SelectCheckoutLayoutStyle) {
        this.style = style
        this.methods.clear()
        methods?.let {
            this.methods.addAll(methods)
        }
        this.notifyDataSetChanged()
    }

    fun setSelected(selectedMethod: Method?, selectedIssuer: Issuer?) {
        this.selectedMethod = selectedMethod
        this.selectedIssuer = selectedIssuer
        this.notifyDataSetChanged()
    }
}