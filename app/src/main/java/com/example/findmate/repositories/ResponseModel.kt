package com.example.findmate.repositories

import com.google.gson.annotations.SerializedName

open class ResponseModel<T>() {
    @SerializedName("status")
    val status: Status? = null

    @SerializedName("data")
    val data: T? = null

    data class Status(
        @SerializedName("message")
        val message: String,
        @SerializedName("details")
        val details: String,
        @SerializedName("timeStamp")
        val timeStamp: Long
    )
}
