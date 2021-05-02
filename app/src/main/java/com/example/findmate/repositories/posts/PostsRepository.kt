package com.example.findmate.repositories.posts

import com.example.findmate.repositories.Api
import com.example.findmate.repositories.ResponseModel
import com.example.findmate.repositories.ServerResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class PostsRepository @Inject constructor(
    val gson: Gson,
    val api: Api
) {
    suspend fun createPost(post: CreatePost): ServerResponse<ResponseModel<Any?>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.createPost(post)
                ServerResponse.SuccessResponse(response)
            } catch (ex: Exception) {
                ServerResponse.ErrorResponse(ex.message ?: "error")
            }
        }
    }

    suspend fun getPostById(id: String): ServerResponse<GetPostResponseModel> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getPostById(id)
                ServerResponse.SuccessResponse(response)
            } catch (ex: Exception) {
                ServerResponse.ErrorResponse(ex.message ?: "error")
            }
        }
    }

    suspend fun getPostByLocation(location: String?, page: Int): ServerResponse<SearchPostResponseModel> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getPostsByLocation(location, page)
                ServerResponse.SuccessResponse(response)
            } catch (ex: Exception) {
                ServerResponse.ErrorResponse(ex.message ?: "error")
            }
        }
    }

    suspend fun getPosts(): ServerResponse<ResponseGetPostsModel> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getPosts()
                ServerResponse.SuccessResponse(response)
            } catch (ex: java.lang.Exception) {
                ServerResponse.ErrorResponse(ex.message ?: "error")
            }
        }
    }
}