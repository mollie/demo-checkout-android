package com.mollie.checkout.feature.payments.list

import androidx.recyclerview.widget.RecyclerView
import com.mollie.checkout.data.model.Section
import com.mollie.checkout.databinding.ItemSectionBinding

class SectionViewHolder(
    private val binding: ItemSectionBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun render(section: Section) {
        binding.section = section
        binding.executePendingBindings()
    }
}