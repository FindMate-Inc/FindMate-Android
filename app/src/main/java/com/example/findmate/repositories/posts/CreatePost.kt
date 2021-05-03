package com.example.findmate.repositories.posts

import com.google.gson.annotations.SerializedName

data class CreatePost (
    @SerializedName("text")
    val text: String,
    @SerializedName("locations")
    val location: List<String>,
    @SerializedName("age")
    val age: Int,
    @SerializedName("sex")
    val sex: Int
)