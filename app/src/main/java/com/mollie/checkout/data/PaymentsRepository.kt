package com.mollie.checkout.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mollie.checkout.data.model.*
import com.mollie.checkout.data.net.ApiService
import com.mollie.checkout.data.net.Connectivity
import java.util.*

object PaymentsRepository {

    private val payments = MutableLiveData<List<Payment>?>()

    fun getPayments(): LiveData<List<Payment>?> = payments

    suspend fun retrievePayment(id: Int): RepositoryResponse<MollieResponse<Payment>> {
        return try {
            val service: ApiService = Connectivity.getRetrofit().create(ApiService::class.java)
            val response = service.getPayment(id)
            RepositoryResponse(response)
        } catch (e: Exception) {
            RepositoryResponse(e)
        }
    }

    suspend fun reloadPayments(): RepositoryResponse<MollieResponse<List<Payment>>> {
        return try {
            val service: ApiService = Connectivity.getRetrofit().create(ApiService::class.java)
            val response = service.getPayments()
            payments.value = response.body()?.data
            RepositoryResponse(response)
        } catch (e: Exception) {
            RepositoryResponse(e)
        }
    }

    suspend fun getMethods(
        amount: Double?
    ): RepositoryResponse<MollieResponse<List<Method>>> {
        return try {
            val service: ApiService = Connectivity.getRetrofit().create(ApiService::class.java)
            val response = service.getMethods(amount)
            RepositoryResponse(response)
        } catch (e: Exception) {
            RepositoryResponse(e)
        }
    }

    suspend fun createPayment(
        amount: Double,
        description: String,
        method: Method? = null,
        issuer: Issuer? = null
    ): RepositoryResponse<MollieResponse<Payment>> {
        val payment = Payment(
            amount = amount,
            description = description,
            method = method?.id,
            issuer = issuer?.id,
            created = Date()
        )
        return try {
            val service: ApiService = Connectivity.getRetrofit().create(ApiService::class.java)
            val response = service.createPayment(payment)

            val createdPayment = response.body()?.data
            if (response.isSuccessful && createdPayment != null) {
                payments.value = listOf(createdPayment) + (payments.value ?: listOf())
            }

            RepositoryResponse(response)
        } catch (e: Exception) {
            RepositoryResponse(e)
        }
    }
}