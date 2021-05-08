package com.abstractclass.findmate.repositories

import com.abstractclass.findmate.repositories.posts.CreatePost
import com.abstractclass.findmate.repositories.posts.GetPostResponseModel
import com.abstractclass.findmate.repositories.posts.ResponseGetPostsModel
import com.abstractclass.findmate.repositories.posts.SearchPostResponseModel
import retrofit2.http.*

interface Api {
    @GET("posts")
    suspend fun getPosts(): ResponseGetPostsModel

    @GET("posts/{id}")
    suspend fun getPostById(@Path("id") name: String): GetPostResponseModel

    @GET("posts")
    suspend fun getPostsByLocation(@Query("location") location: String?, @Query("page") page: Int): SearchPostResponseModel

    @POST("posts")
    suspend fun createPost(@Body createPost: CreatePost): ResponseModel<Any?>
}