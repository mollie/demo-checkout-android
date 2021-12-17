package com.mollie.checkout.feature.base

import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.snackbar.Snackbar
import com.mollie.checkout.BR

abstract class BaseActivity<DB : ViewDataBinding> : AppCompatActivity() {

    lateinit var binding: DB

    @LayoutRes
    abstract fun getLayout(): Int

    abstract fun getViewModel(): BaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, getLayout())
        binding.setVariable(BR.viewModel, getViewModel())
        binding.lifecycleOwner = this

        setupBase()
    }

    private fun setupBase() {
        getViewModel().getError().observe(this) { message ->
            when (message) {
                is String -> showMessage(message)
                is Int -> showMessage(getString(message))
            }
        }
    }

    private fun showMessage(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}