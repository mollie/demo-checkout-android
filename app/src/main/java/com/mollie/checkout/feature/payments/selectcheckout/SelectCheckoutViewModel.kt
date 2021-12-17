package com.mollie.checkout.feature.payments.selectcheckout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mollie.checkout.R
import com.mollie.checkout.data.PaymentsRepository
import com.mollie.checkout.data.model.Issuer
import com.mollie.checkout.data.model.Method
import com.mollie.checkout.data.model.Payment
import com.mollie.checkout.data.model.SelectCheckoutLayoutStyle
import com.mollie.checkout.feature.base.BaseViewModel
import kotlinx.coroutines.launch

class SelectCheckoutViewModel : BaseViewModel() {

    private val state = MutableLiveData<SelectCheckoutState>()
    var style = SelectCheckoutLayoutStyle.LIST
        private set
    private val methods = MutableLiveData<List<Method>?>()
    private val createdPayment = MutableLiveData<Payment?>()
    private val selection = MutableLiveData<Pair<Method, Issuer?>?>()
    private var initialPayment: Payment? = null
    private val saving = MutableLiveData<Boolean>()

    init {
        state.value = SelectCheckoutState.SELECT_METHOD
    }

    fun getState(): LiveData<SelectCheckoutState> = state

    fun getMethods(): LiveData<List<Method>?> = methods

    fun getSelection(): LiveData<Pair<Method, Issuer?>?> = selection

    fun isSaving(): LiveData<Boolean> = saving

    fun getCreatedPayment(): LiveData<Payment?> = createdPayment

    fun load(payment: Payment?) {
        initialPayment = payment
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            setLoading(true)
            val response = PaymentsRepository.getMethods(initialPayment?.amount)
            if (response.isSuccess()) {
                methods.value = response.getData()?.data
            } else {
                methods.value = null
                setError(response.getError())
            }
            setLoading(false)
        }
    }

    fun onBack(): Boolean {
        return if (state.value == SelectCheckoutState.SELECT_ISSUER) {
            selection.value = selection.value?.copy(second = null)
            state.value = SelectCheckoutState.SELECT_METHOD
            true
        } else {
            false
        }
    }

    fun onSelectedMethod(method: Method) {
        if (selection.value?.first == method) {
            selection.value = null
        } else {
            selection.value = Pair(method, null)
        }
    }

    fun onSelectedIssuer(issuer: Issuer) {
        selection.value = selection.value?.copy(second = issuer)
    }

    fun proceed() {
        if (selection.value?.first == null) {
            setError(R.string.select_checkout_method_missing)
            return
        }
        val hasSelectedMethodIssuers = !selection.value?.first?.issuers.isNullOrEmpty()
        val hasSelectedIssuer = selection.value?.second != null
        if (hasSelectedMethodIssuers && !hasSelectedIssuer) {
            if (state.value == SelectCheckoutState.SELECT_ISSUER) {
                setError(R.string.select_checkout_issuer_missing)
            } else {
                state.value = SelectCheckoutState.SELECT_ISSUER
            }
        } else {
            submit()
        }
    }

    private fun submit() {
        val initialPayment = initialPayment
        if (initialPayment == null) {
            setError(R.string.select_checkout_payment_missing)
            return
        }
        viewModelScope.launch {
            saving.value = true
            val response = PaymentsRepository.createPayment(
                initialPayment.amount,
                initialPayment.description,
                selection.value?.first,
                selection.value?.second
            )
            if (response.isSuccess()) {
                state.value = SelectCheckoutState.PAYMENT_CREATED
                createdPayment.value = response.getData()?.data
            } else {
                createdPayment.value = null
                setError(response.getError())
            }
            saving.value = false
        }
    }

    fun onSetLayout(style: SelectCheckoutLayoutStyle?) {
        this.style = style ?: this.style
        this.selection.value = this.selection.value?.copy(second = null) // Clear issuer selection
        this.methods.value = this.methods.value // Trigger update
    }
}