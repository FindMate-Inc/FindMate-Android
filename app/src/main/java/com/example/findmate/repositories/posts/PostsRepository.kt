package com.example.findmate.repositories.posts

import com.example.findmate.repositories.Api
import com.example.findmate.repositories.ResponseModel
import com.example.findmate.repositories.ServerResponse
import com.google.gson.Gson
import javax.inject.Inject


class PostsRepository @Inject constructor(
    val gson: Gson,
    val api: Api
) {
    suspend fun createPost(post: CreatePost): ServerResponse<ResponseModel<Any?>> {
        return try {
            val response = api.createPost(post)
            ServerResponse.SuccessResponse(response)
        } catch (ex: Exception) {
            ServerResponse.ErrorResponse(ex.message ?: "error")
        }
    }

    suspend fun getPostById(id: String): ServerResponse<GetPostResponseModel> {
        return try {
            val response = api.getPostById(id)
            ServerResponse.SuccessResponse(response)
        } catch (ex: Exception) {
            ServerResponse.ErrorResponse(ex.message ?: "error")
        }
    }

    suspend fun getPostByLocation(location: String): ServerResponse<SearchPostResponseModel> {
        return try {
            val response = api.getPostsByLocation(location)
            ServerResponse.SuccessResponse(response)
        } catch (ex: Exception) {
            ServerResponse.ErrorResponse(ex.message ?: "error")
        }
    }
}