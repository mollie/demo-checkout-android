package com.mollie.checkout.feature.splash

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mollie.checkout.data.PaymentsRepository
import com.mollie.checkout.data.PreferenceRepository
import com.mollie.checkout.data.net.Connectivity
import com.mollie.checkout.feature.base.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class SplashViewModel : BaseViewModel() {

    companion object {
        private const val SPLASH_DELAY = 750L
    }

    private val state = MutableLiveData<SplashState>()

    init {
        state.value = SplashState.LOADING
    }

    fun getState(): LiveData<SplashState> = state

    private fun loadPayments() {
        viewModelScope.launch {
            PaymentsRepository.reloadPayments()
            delay(SPLASH_DELAY)
            state.value = SplashState.PAYMENTS
        }
    }

    fun load(context: Context) {
        state.value = SplashState.LOADING

        ensureDeviceUUID(context)
        loadPayments()
    }

    private fun ensureDeviceUUID(context: Context) {
        val deviceUUID = PreferenceRepository.getDeviceUUID(context)
        if (deviceUUID == null) {
            val newUUID = UUID.randomUUID().toString()
            PreferenceRepository.setDeviceUUID(context, newUUID)
            Connectivity.setDeviceUUID(newUUID)
        } else {
            Connectivity.setDeviceUUID(deviceUUID)
        }
    }
}