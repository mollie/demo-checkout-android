package com.mollie.checkout.feature.payments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.mollie.checkout.R
import com.mollie.checkout.data.model.Payment
import com.mollie.checkout.databinding.ActivityPaymentsBinding
import com.mollie.checkout.feature.Navigation
import com.mollie.checkout.feature.base.BaseActivity
import com.mollie.checkout.feature.components.MollieAlert
import com.mollie.checkout.feature.payments.list.PaymentsAdapter

class PaymentsActivity : BaseActivity<ActivityPaymentsBinding>(), PaymentsAdapter.Listener {

    companion object {
        const val EXTRA_COMPLETED_PAYMENT_ID = "completed_payment_id"
    }

    private val vm: PaymentsViewModel by viewModels()

    override fun getLayout() = R.layout.activity_payments
    override fun getViewModel() = vm

    private val adapter = PaymentsAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupView()
        setupListeners()
        setupObservers()

        checkCompletedPayment(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        checkCompletedPayment(intent)
    }

    override fun onResume() {
        super.onResume()
        vm.refresh() // Refreshing the payments list when opening this screen to ensure latest state of the payments
    }

    /**
     * Checks whether this screen was opened because the full payment flow has been followed.
     * Retrieving the payment id and allows reloading that payment if needed.
     *
     * <b>Note:</b> while this works to check for completed payments, there are cases where the user
     * breaks the payment flow between applications and might just return to the app manually. When
     * this happens, there is no deeplink or completed payment note. When this happens, the only way
     * to know the latest state of the purchase is by reloading the payment itself. In this project
     * we reload the full payments list in the `onResume()` method to always show the latest state
     * of the payments.
     */
    private fun checkCompletedPayment(intent: Intent?) {
        if (intent == null) {
            // We don't need to check for this intent, it is not a completed payment for sure
            return
        }

        val isFromDeeplink = intent.data?.scheme == getString(R.string.deeplink_scheme_app)
        val completedPaymentId = intent.getIntExtra(EXTRA_COMPLETED_PAYMENT_ID, -1)
        val isFromCompletedPayment = completedPaymentId != -1

        when {
            isFromDeeplink -> {
                // Payment data when called from deeplink
                val paymentId = intent.data?.path?.substring(1) // `path` starts with a `/`, removing that
                // vm.refresh() // Reload the payment after returning. Not needed in this case since we reload the full list in onResume.
            }
            isFromCompletedPayment -> {
                // Payment data when called from a completed payment
                val paymentId = completedPaymentId
                // vm.refresh() // Reload the payment after returning. Not needed in this case since we reload the full list in onResume.
            }
            else -> {
                // Opened this screen without a completed payment
            }
        }
    }

    private fun setupView() {
        setTitle(R.string.payments_title)

        binding.recyclerView.adapter = adapter
    }

    private fun setupListeners() {
        binding.refreshLayout.setOnRefreshListener {
            vm.refresh()
        }
        binding.createFirstPayment.setOnClickListener {
            Navigation.openCreatePayment(this)
        }
        binding.createPayment.setOnClickListener {
            Navigation.openCreatePayment(this)
        }
    }

    private fun setupObservers() {
        vm.isLoading().observe(this) { loading ->
            binding.refreshLayout.isRefreshing = loading
        }
        vm.getPaymentItems().observe(this) { payments ->
            adapter.show(payments)
        }
    }

    override fun onPaymentClicked(payment: Payment) {
        when {
            payment.status?.isCompleted == true -> {
                MollieAlert.show(
                    this,
                    getString(R.string.payment_status_title),
                    getString(
                        R.string.payment_status_description,
                        getString(payment.status.label)
                    ),
                    primary = getString(R.string.general_ok),
                )
            }
            payment.url == null -> {
                Log.e("$javaClass", "Missing payment url")
            }
            else -> {
                Navigation.openPaymentFlow(this, payment)
            }
        }
    }
}