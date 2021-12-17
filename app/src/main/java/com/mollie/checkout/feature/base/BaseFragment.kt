package com.mollie.checkout.feature.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.mollie.checkout.BR

abstract class BaseFragment<DB : ViewDataBinding> : Fragment() {

    lateinit var binding: DB

    @LayoutRes
    abstract fun getLayout(): Int

    abstract fun getViewModel(): BaseViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getLayout(), container, false)
        binding.lifecycleOwner = this

        binding.setVariable(BR.viewModel, getViewModel())

        setupBase()
        setup()

        return binding.root
    }

    abstract fun setup()

    private fun setupBase() {
        getViewModel().getError().observe(viewLifecycleOwner) { message ->
            when (message) {
                is String -> showMessage(message)
                is Int -> showMessage(getString(message))
            }
        }
    }

    private fun showMessage(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }
}