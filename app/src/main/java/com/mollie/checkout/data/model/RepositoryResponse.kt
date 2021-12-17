package com.mollie.checkout.data.model

import retrofit2.Response

data class RepositoryResponse<T>(
    private val response: Response<T>?,
    private val exception: Exception?,
) {

    constructor(response: Response<T>) : this(response, null)
    constructor(exception: Exception) : this(null, exception)

    fun isSuccess(): Boolean {
        return response?.isSuccessful == true
    }

    fun getData(): T? {
        return response?.body()
    }

    fun getError(): String {
        return if (response == null) {
            exception?.message
        } else {
            "(${response.code()}) ${response.message()}"
        } ?: "Unknown error"
    }
}