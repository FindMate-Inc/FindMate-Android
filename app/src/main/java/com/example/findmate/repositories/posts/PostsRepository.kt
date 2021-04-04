package com.example.findmate.repositories.posts

import android.util.Log
import com.example.findmate.repositories.Api
import com.google.gson.Gson
import retrofit2.Retrofit
import javax.inject.Inject


class PostsRepository @Inject constructor(
        val gson: Gson,
        val api: Api
) {

    suspend fun provideTwo(id: String): PostResponse {
        return api.getPostById(id)
    }
}