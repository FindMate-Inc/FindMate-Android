package com.abstractclass.findmate.repositories

import com.google.gson.annotations.SerializedName

open class ResponseModel<T>() {
    @SerializedName("status")
    val status: Status? = null

    @SerializedName("data")
    val data: T? = null

    data class Status(
        @SerializedName("success")
        val success: Boolean,
        @SerializedName("errorMessage")
        val errorMessage: String,
        @SerializedName("errorCode")
        val errorCode: String,
        @SerializedName("timestamp")
        val timestamp: Long
    )
}
