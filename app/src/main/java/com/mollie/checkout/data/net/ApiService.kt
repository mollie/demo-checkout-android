package com.mollie.checkout.data.net

import com.mollie.checkout.data.model.Method
import com.mollie.checkout.data.model.MollieResponse
import com.mollie.checkout.data.model.Payment
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("payments")
    suspend fun createPayment(
        @Body payment: Payment
    ): Response<MollieResponse<Payment>>

    @GET("payments")
    suspend fun getPayments(): Response<MollieResponse<List<Payment>>>

    @GET("payments/{id}")
    suspend fun getPayment(
        @Path("id") id: Int
    ): Response<MollieResponse<Payment>>

    @GET("methods")
    suspend fun getMethods(
        @Query("amount") amount: Double?
    ): Response<MollieResponse<List<Method>>>
}