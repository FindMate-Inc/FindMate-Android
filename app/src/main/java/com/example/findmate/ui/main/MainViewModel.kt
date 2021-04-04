package com.example.findmate.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findmate.repositories.ServerResponse
import com.example.findmate.repositories.posts.CreatePost
import com.example.findmate.repositories.posts.PostsRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(val postRepository: PostsRepository) : ViewModel() {

    fun check() {
        viewModelScope.launch {
            val response = postRepository.getPostById("507f1f77bcf86cd799439022")
            when (response) {
                is ServerResponse.SuccessResponse -> Log.d("TestPish", "${response.response.data}")
                is ServerResponse.ErrorResponse -> Log.d("TestPish", "${response.errorMessage}")
            }
        }

        viewModelScope.launch {
            val response = postRepository.getPostByLocation("%D0%9A%D0%B8%D0%B5%D0%B2")
            when (response) {
                is ServerResponse.SuccessResponse -> Log.d("TestPish", "${response.response.data}")
                is ServerResponse.ErrorResponse -> Log.d("TestPish", "${response.errorMessage}")
            }
        }

        viewModelScope.launch {
            val response = postRepository.createPost(CreatePost(text = "text", location = "location", age = 30, sex = 2))
            when (response) {
                is ServerResponse.SuccessResponse -> Log.d("TestPish", "${response.response.data}")
                is ServerResponse.ErrorResponse -> Log.d("TestPish", "${response.errorMessage}")
            }
        }
    }
}