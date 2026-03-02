package com.company.model.common

data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val message: String? = null
) {
    companion object {
        fun <T> ok(data: T): ApiResponse<T> = ApiResponse(success = true, data = data)
        fun <T> ok(): ApiResponse<T> = ApiResponse(success = true)
        fun <T> fail(message: String): ApiResponse<T> = ApiResponse(success = false, message = message)
    }
}
