package com.mollie.checkout.feature.payments.selectcheckout

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.mollie.checkout.R
import com.mollie.checkout.data.model.Payment
import com.mollie.checkout.databinding.ActivitySelectCheckoutBinding
import com.mollie.checkout.feature.Navigation
import com.mollie.checkout.feature.base.BaseActivity
import kotlin.reflect.KClass

class SelectCheckoutActivity : BaseActivity<ActivitySelectCheckoutBinding>() {

    companion object {
        const val EXTRA_PAYMENT = "payment"
    }

    private val vm: SelectCheckoutViewModel by viewModels()

    override fun getLayout() = R.layout.activity_select_checkout
    override fun getViewModel() = vm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupView()
        setupObservers()
        vm.load(getPayment())
    }

    private fun getPayment(): Payment? {
        return intent.getParcelableExtra(EXTRA_PAYMENT)
    }

    private fun setupView() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupObservers() {
        vm.getState().observe(this) { state ->
            when (state) {
                SelectCheckoutState.SELECT_METHOD -> {
                    setTitle(R.string.select_method_title)
                    showFragment(SelectMethodFragment::class)
                }
                SelectCheckoutState.SELECT_ISSUER -> {
                    setTitle(R.string.select_issuer_title)
                    showFragment(SelectIssuerFragment::class)
                }
                SelectCheckoutState.PAYMENT_CREATED -> {
                    // Payment creation will handle this
                }
                else -> {
                    Log.d("$javaClass", "Unhandled state: $state")
                }
            }
        }
        vm.getCreatedPayment().observe(this) { payment ->
            if (payment != null) {
                Navigation.openPaymentFlow(this, payment) {
                    finish()
                }
            }
        }
    }

    private fun showFragment(fragmentClass: KClass<*>) {
        val fragment = fragmentClass.java.newInstance() as Fragment
        supportFragmentManager.beginTransaction()
            .replace(binding.content.id, fragment)
            .commit()
    }

    @Deprecated("Deprecated in Java")
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        if (vm.onBack()) {
            return
        } else {
            Navigation.openCreatePayment(this, getPayment())
            finish()
        }
    }
}