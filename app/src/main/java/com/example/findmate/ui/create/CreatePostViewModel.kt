package com.example.findmate.ui.create

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findmate.repositories.ServerResponse
import com.example.findmate.repositories.posts.CreatePost
import com.example.findmate.repositories.posts.Post
import com.example.findmate.repositories.posts.PostsRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class CreatePostViewModel @Inject constructor(val postRepository: PostsRepository) : ViewModel() {
    fun createPost(age: String, location: String, sex: String, text: String) {
        viewModelScope.launch {
            val response = postRepository.createPost(
                CreatePost(
                    text = text,
                    location = location,
                    age = age.toInt(),
                    sex = sex.toInt()
                )
            )

            when (response) {
                is ServerResponse.SuccessResponse -> Log.d("TestPish", "${response.response.data}")
                is ServerResponse.ErrorResponse -> Log.d("TestPish", "${response.errorMessage}")
            }
        }
    }

    val posts = MutableLiveData<List<Post>>()
}