package com.example.findmate.ui.main

import android.R
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findmate.repositories.posts.PostsRepository
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.coroutineContext
import kotlin.coroutines.suspendCoroutine

class MainViewModel @Inject constructor(val postRepository: PostsRepository) : ViewModel() {

    fun check() {
        viewModelScope.launch {
            Log.d("TestPish", ""+postRepository.provideTwo("507f1f77bcf86cd799439022").data)
        }
    }
}