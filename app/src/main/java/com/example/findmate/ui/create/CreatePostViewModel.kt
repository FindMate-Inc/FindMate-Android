package com.example.findmate.ui.create

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findmate.repositories.ServerResponse
import com.example.findmate.repositories.posts.CreatePost
import com.example.findmate.repositories.posts.Post
import com.example.findmate.repositories.posts.PostsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class CreatePostViewModel @Inject constructor(val postRepository: PostsRepository) : ViewModel() {

    val age = MutableLiveData<String>(null)
    val sex = MutableLiveData<String>(null)
    val location = MutableLiveData<String>(null)
    val text = MutableLiveData<String>("")

    val textError = MutableLiveData<Boolean>(false)

    fun createPost() {
        val textLength = text.value?.length?:0
        if (textLength < 25 || textLength > 4000) {
            textError.value = true
            text.observeForever {
                if (it.length in 26..4000) {
                    textError.value = false
                }
            }
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val response = postRepository.createPost(
                CreatePost(
                    text = text.value!!,
                    location = listOf(location.value?:"Интернет"),
                    age = age.value?.toInt() ?: 1,
                    sex = sex.value?.toInt() ?: 3
                )
            )

            when (response) {
                is ServerResponse.SuccessResponse -> Log.d("TestPish", "${response.response.data}")
                is ServerResponse.ErrorResponse -> Log.d("TestPish", "${response.errorMessage}")
            }
        }
    }
}