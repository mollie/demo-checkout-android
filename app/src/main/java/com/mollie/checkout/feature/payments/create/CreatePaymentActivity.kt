package com.mollie.checkout.feature.payments.create

import android.os.Bundle
import android.text.method.DigitsKeyListener
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.mollie.checkout.R
import com.mollie.checkout.Settings
import com.mollie.checkout.data.model.Payment
import com.mollie.checkout.databinding.ActivityPaymentCreateBinding
import com.mollie.checkout.feature.Navigation
import com.mollie.checkout.feature.base.BaseActivity
import com.mollie.checkout.feature.payments.selectcheckout.SelectCheckoutActivity
import com.mollie.checkout.util.Formatting
import com.mollie.checkout.util.asColorStateList
import kotlinx.coroutines.launch
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.text.ParseException


class CreatePaymentActivity : BaseActivity<ActivityPaymentCreateBinding>() {

    companion object {
        const val EXTRA_PAYMENT = "payment"
    }

    private val vm: CreatePaymentViewModel by viewModels()

    override fun getLayout() = R.layout.activity_payment_create
    override fun getViewModel() = vm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupView()
        setupListeners()
        setupObservers()
        vm.load(getPayment())
        invalidateOptionsMenu()
    }

    private fun getPayment(): Payment? {
        return intent.getParcelableExtra(SelectCheckoutActivity.EXTRA_PAYMENT)
    }

    private fun setupView() {
        setTitle(R.string.create_payment_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val separator = DecimalFormatSymbols.getInstance().decimalSeparator
        binding.amount.editText?.keyListener = DigitsKeyListener.getInstance("0123456789$separator")
    }

    private fun setupListeners() {
        binding.description.editText?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                submit()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
        binding.amount.editText?.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val formatted = Formatting.getFormattedPrice(getInputAmount(), withCurrency = false)
                binding.amount.editText?.setText(formatted)
            }
        }
    }

    private fun setupObservers() {
        vm.getInitialProduct().observe(this) {
            val description = it?.first
            val amount = it?.second
            binding.description.editText?.setText(description)
            binding.amount.editText?.setText(
                Formatting.getFormattedPrice(
                    amount,
                    withCurrency = false
                )
            )
        }
    }

    private fun submit() {
        currentFocus?.clearFocus()
        val initialPayment = vm.createPayment(
            getInputAmount(),
            binding.description.editText?.text?.toString()
        )
        if (initialPayment != null) {
            if (Settings.Navigation.SELECT_PAYMENT_METHOD) {
                Navigation.openSelectCheckout(this@CreatePaymentActivity, initialPayment)
                finish()
            } else {
                lifecycleScope.launch {
                    val payment = vm.submit(initialPayment)
                    if (payment != null) {
                        Navigation.openPaymentFlow(this@CreatePaymentActivity, payment) {
                            finish()
                        }
                    }
                }
            }
        }
    }

    private fun getInputAmount(): Double? {
        val amountText = binding.amount.editText?.text?.toString()
        return try {
            NumberFormat.getInstance().parse(amountText ?: "")
        } catch (e: ParseException) {
            e.printStackTrace()
            Log.e("${this.javaClass}", "Couldn't parse amount: $amountText")
            null
        }?.toDouble()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.create, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_create -> {
                submit()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        (findViewById(R.id.menu_create) as? TextView)?.setTextColor(
            R.color.colorPrimary.asColorStateList(
                this
            )
        )
        return super.onPrepareOptionsMenu(menu)
    }
}