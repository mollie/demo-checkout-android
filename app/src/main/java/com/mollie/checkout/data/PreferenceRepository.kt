package com.mollie.checkout.data

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

object PreferenceRepository {

    private const val KEY_DEVICE_UUID = "device_uuid"

    private fun getPrefs(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun setDeviceUUID(context: Context, uuid: String) {
        getPrefs(context)
            .edit()
            .putString(KEY_DEVICE_UUID, uuid)
            .apply()
    }

    fun getDeviceUUID(context: Context): String? {
        return getPrefs(context)
            .getString(KEY_DEVICE_UUID, null)
    }
}