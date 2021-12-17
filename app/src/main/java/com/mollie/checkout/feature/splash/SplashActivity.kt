package com.mollie.checkout.feature.splash

import android.os.Bundle
import androidx.activity.viewModels
import com.mollie.checkout.R
import com.mollie.checkout.databinding.ActivitySplashBinding
import com.mollie.checkout.feature.Navigation
import com.mollie.checkout.feature.base.BaseActivity

class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    private val vm: SplashViewModel by viewModels()

    override fun getLayout() = R.layout.activity_splash
    override fun getViewModel() = vm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupObservers()
        loadData()
    }

    private fun setupObservers() {
        vm.getState().observe(this) { state ->
            when (state) {
                SplashState.PAYMENTS -> {
                    Navigation.openPayments(this)
                    finish()
                }
                else -> {
                    // Still loading
                }
            }
        }
    }

    private fun loadData() {
        vm.load(this)
    }
}