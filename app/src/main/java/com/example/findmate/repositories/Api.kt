package com.example.findmate.repositories

import com.example.findmate.repositories.posts.PostResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    @GET("posts/{id}")
    suspend fun getPostById(@Path("id") name: String): PostResponse
}