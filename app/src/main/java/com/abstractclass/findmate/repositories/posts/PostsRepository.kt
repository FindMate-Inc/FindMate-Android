package com.abstractclass.findmate.repositories.posts

import com.abstractclass.findmate.repositories.Api
import com.abstractclass.findmate.repositories.ResponseModel
import com.abstractclass.findmate.repositories.ServerResponse
import com.abstractclass.findmate.repositories.posts.create.CreatePost
import com.abstractclass.findmate.repositories.posts.list.SearchPostResponseModel
import com.abstractclass.findmate.repositories.posts.report.PostReportRequest
import com.abstractclass.findmate.repositories.posts.report.PostReportResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class PostsRepository @Inject constructor(
    private val api: Api
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

    suspend fun reportPost(userId:String, postId: String): ServerResponse<PostReportResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.reportPost(postId, PostReportRequest(userId))
                ServerResponse.SuccessResponse(response)
            } catch (ex: Exception) {
                ServerResponse.ErrorResponse(ex.message ?: "error")
            }
        }
    }
}