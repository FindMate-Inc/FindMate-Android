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
        viewModelScope.launch {
            currentPage = 1
            val newPosts = loadPosts(searchLocation.value, currentPage)

            when {
                newPosts == null -> {
                    screenState.value = States.ERROR
                }
                newPosts.isEmpty() -> {
                    screenState.value = States.NO_POSTS
                }
                else -> {
                    screenState.value = States.DEFAULT
                    posts.value = newPosts!!
                }
            }
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
                ) {
                    return null
                }

                currentPage = response.response.pagination.page
                totalPages = response.response.pagination.pagesCount
                return filterNotBlacklistedPosts(response.response.data!!)
            }

            is ServerResponse.ErrorResponse -> {
                return null
            }
        }
    }

    private fun filterNotBlacklistedPosts(posts: List<Post>): List<Post> {
        return posts.filter { post ->
            val isReportedByUser = androidId in post.reports.reportedBy
            val isBlacklisted = post.reports.isBlacklisted
            !isReportedByUser && !isBlacklisted
        }
    }

    enum class States {
        DEFAULT, LOADING, ERROR, NO_POSTS
    }

    companion object {
        const val THRESHOLD = 4
    }
}