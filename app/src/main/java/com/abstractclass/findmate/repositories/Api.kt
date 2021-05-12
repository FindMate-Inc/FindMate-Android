package com.abstractclass.findmate.repositories

import com.abstractclass.findmate.repositories.posts.create.CreatePost
import com.abstractclass.findmate.repositories.posts.list.SearchPostResponseModel
import com.abstractclass.findmate.repositories.posts.report.PostReportRequest
import com.abstractclass.findmate.repositories.posts.report.PostReportResponse
import retrofit2.http.*

interface Api {
    @GET("posts")
    suspend fun getPostsByLocation(@Query("location") location: String?, @Query("page") page: Int): SearchPostResponseModel?

    @POST("posts")
    suspend fun createPost(@Body createPost: CreatePost): ResponseModel<Any?>

    @PUT("posts/{id}/report")
    suspend fun reportPost(@Path("id") postId: String, @Body postReportRequest: PostReportRequest): PostReportResponse
}