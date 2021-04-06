package com.example.findmate.repositories.posts

import com.google.gson.annotations.SerializedName

data class Post(
    @SerializedName("_id")
    val id: String,
    @SerializedName("text")
    val text: String,
    @SerializedName("locations")
    val locations: List<String>,
    @SerializedName("age")
    val age: Int,
    @SerializedName("sex")
    val sex: Int,
    @SerializedName("createdAt")
    val createdAt: Long
)