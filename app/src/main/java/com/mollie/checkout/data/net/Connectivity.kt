package com.mollie.checkout.data.net

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mollie.checkout.Settings
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Connectivity {

    private const val HEADER_DEVICE_UUID = "X-Mollie-Checkout-Device-UUID"

    private var deviceUUID: String? = null

    fun setDeviceUUID(uuid: String) {
        this.deviceUUID = uuid
    }

    @Suppress("SpellCheckingInspection")
    private fun getGson(): Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
        return gsonBuilder.create()
    }

    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Settings.Network.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(getGson()))
            .client(getOkHttpClient())
            .build()
    }

    private fun getOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor { chain ->
            val original = chain.request()

            val requestBuilder = original.newBuilder()
                .method(original.method(), original.body())

            deviceUUID?.let {
                requestBuilder.addHeader(HEADER_DEVICE_UUID, it)
            }

            val request: Request = requestBuilder.build()
            chain.proceed(request)
        }
        return builder.build()
    }
}