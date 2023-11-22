package com.mollie.checkout.feature.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.mollie.checkout.R
import com.mollie.checkout.databinding.ActivitySplashBinding
import com.mollie.checkout.feature.Navigation
import com.mollie.checkout.feature.base.BaseActivity
import com.mollie.checkout.feature.splash.SplashViewModel.Companion.AnimationFadeDuration

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    private val vm: SplashViewModel by viewModels()

    override fun getLayout() = R.layout.activity_splash
    override fun getViewModel() = vm

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setOnExitAnimationListener { splashScreenViewProvider ->
            splashScreenViewProvider
                .view
                .animate()
                .alpha(0f)
                .setDuration(AnimationFadeDuration)
                .withEndAction {
                    splashScreenViewProvider.remove()
                }
                .start()
        }

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