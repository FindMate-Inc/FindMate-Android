package com.example.findmate.repositories

import com.example.findmate.repositories.posts.CreatePost
import com.example.findmate.repositories.posts.GetPostResponseModel
import com.example.findmate.repositories.posts.SearchPostResponseModel
import retrofit2.http.*

interface Api {
    @GET("posts/{id}")
    suspend fun getPostById(@Path("id") name: String): GetPostResponseModel

    @GET("posts/search")
    suspend fun getPostsByLocation(@Query("location") location: String): SearchPostResponseModel

    @POST("posts")
    suspend fun createPost(@Body createPost: CreatePost): ResponseModel<Any?>
}