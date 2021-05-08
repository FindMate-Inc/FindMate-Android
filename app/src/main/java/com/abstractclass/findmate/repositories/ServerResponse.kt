package com.abstractclass.findmate.repositories

sealed class ServerResponse<out T: Any?> {
    data class SuccessResponse<T: Any>(val response: T): ServerResponse<T>()
    data class ErrorResponse(val errorMessage: String): ServerResponse<Nothing>()
}