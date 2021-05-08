package com.abstractclass.findmate.ui.create

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abstractclass.findmate.repositories.ServerResponse
import com.abstractclass.findmate.repositories.posts.create.CreatePost
import com.abstractclass.findmate.repositories.posts.PostsRepository
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
        if (!isValid()) return

        viewModelScope.launch(Dispatchers.IO) {
            val response = postRepository.createPost(
                CreatePost(
                    text = text.value!!,
                    location = parseLocations(),
                    age = parseAge(),
                    sex = parseSex()
                )
            )

            when (response) {
                is ServerResponse.SuccessResponse -> Log.d("TestPish", "${response.response.data}")
                is ServerResponse.ErrorResponse -> Log.d("TestPish", "${response.errorMessage}")
            }
        }
    }

    private fun isValid():Boolean {
        val textLength = text.value?.length?:0
        if (textLength < 25 || textLength > 4000) {
            textError.value = true
            text.observeForever {
                if (it.length in 26..4000) {
                    textError.value = false
                }
            }
            return false
        }
        return true
    }

    private fun parseLocations(): List<String> {
        val locationString = location.value
        if (locationString.isNullOrEmpty()) return listOf(DEFAULT_LOCATION)
        return locationString.split(',').toMutableList().map {
            it.trim()
        }
    }

    private fun parseAge(): Int {
        val ageString = age.value
        if (ageString.isNullOrEmpty() || ageString.isBlank()) return DEFAULT_AGE
        return ageString.toInt()
    }

    private fun parseSex(): Int {
        val sexString = sex.value
        if (sexString.isNullOrEmpty() || sexString.isBlank()) return DEFAULT_SEX
        return sexString.toInt()
    }

    companion object {
        const val DEFAULT_LOCATION = "Интернет"
        const val DEFAULT_AGE = 1
        const val DEFAULT_SEX = 3
    }
}