package com.abstractclass.findmate.repositories.posts.create

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