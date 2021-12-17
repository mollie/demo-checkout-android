package com.mollie.checkout.feature.payments

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.mollie.checkout.data.PaymentsRepository
import com.mollie.checkout.data.model.ListItemPayment
import com.mollie.checkout.data.model.Payment
import com.mollie.checkout.data.model.Section
import com.mollie.checkout.feature.base.BaseViewModel
import com.mollie.checkout.util.Formatting
import kotlinx.coroutines.launch

class PaymentsViewModel : BaseViewModel() {

    private val paymentItems = PaymentsRepository.getPayments().map {
        loadPayments(it)
    }

    fun getPaymentItems(): LiveData<List<ListItemPayment>> = paymentItems

    fun refresh() {
        setLoading(true)
        viewModelScope.launch {
            val response = PaymentsRepository.reloadPayments()
            if (response.isSuccess()) {
                clearError()
            } else {
                setError(response.getError())
            }
            setLoading(false)
        }
    }

    private fun loadPayments(payments: List<Payment>?): List<ListItemPayment> {
        val items = mutableListOf<ListItemPayment>()

        val grouped = payments?.groupBy { Formatting.getFormattedDay(it.created) }

        grouped?.forEach { entry ->
            items.add(Section(entry.value.first().created))
            items.addAll(entry.value)
        }

        return items
    }
}