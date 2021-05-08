package com.abstractclass.findmate.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abstractclass.findmate.repositories.ServerResponse
import com.abstractclass.findmate.repositories.posts.Post
import com.abstractclass.findmate.repositories.posts.PostsRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val postRepository: PostsRepository
) : ViewModel() {
    var androidId: String? = null
    val searchLocation = MutableLiveData<String?>(null)
    val posts = MutableLiveData<List<Post>>()
    val screenState = MutableLiveData<States>(States.DEFAULT)

    var currentPage = 1
    var totalPages = 1

    fun clearFilter() {
        searchLocation.value = ""
    }

    fun loadNewPosts() {
        screenState.value = States.LOADING
        viewModelScope.launch {
            currentPage = 1
            val newPosts = loadPosts(searchLocation.value, currentPage)
            posts.value = newPosts ?: emptyList()
            screenState.value = States.DEFAULT
        }
    }

    fun loadMorePosts(lastVisiblePostPosition: Int) {
        val items = posts.value ?: return
        if (lastVisiblePostPosition > items.size - 1 - THRESHOLD && currentPage < totalPages && screenState.value != States.LOADING) {
            screenState.value = States.LOADING
            viewModelScope.launch {
                currentPage++
                val nextPosts = loadPosts(searchLocation.value, currentPage)
                posts.value = items + (nextPosts ?: emptyList())
                screenState.value = States.DEFAULT
            }
        }
    }

    fun reportPost(id: String) {
        viewModelScope.launch {
            if (androidId == null) return@launch

            viewModelScope.launch {
                val reportPostResponse = postRepository.reportPost(androidId!!, id)
            }
        }
    }

    private suspend fun loadPosts(location: String?, page: Int): List<Post>? {
        when (val response = postRepository.getPostByLocation(location, page)) {
            is ServerResponse.SuccessResponse -> {
                if (response.response.pagination?.page == null ||
                    response.response.data == null
                ) return null

                currentPage = response.response.pagination.page
                totalPages = response.response.pagination.pagesCount
                return response.response.data
            }

            is ServerResponse.ErrorResponse -> {
                //show error mb
                return null
            }
        }
    }

    enum class States {
        SEARCH, DEFAULT, LOADING
    }

    companion object {
        const val THRESHOLD = 4
    }
}