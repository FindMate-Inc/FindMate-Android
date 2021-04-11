package com.example.findmate.repositories

import com.example.findmate.repositories.posts.CreatePost
import com.example.findmate.repositories.posts.GetPostResponseModel
import com.example.findmate.repositories.posts.SearchPostResponseModel
import retrofit2.http.*

interface Api {
    @GET("posts/{id}")
    suspend fun getPostById(@Path("id") name: String): GetPostResponseModel

    @GET("https://run.mocky.io/v3/437d3f17-f1c2-473b-b670-8944c361cede")
    suspend fun getPostsByLocation(@Query("location") location: String): SearchPostResponseModel

    @POST("posts")
    suspend fun createPost(@Body createPost: CreatePost): ResponseModel<Any?>
}