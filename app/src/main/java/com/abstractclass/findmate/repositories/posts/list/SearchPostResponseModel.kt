package com.abstractclass.findmate.repositories.posts.list

import com.abstractclass.findmate.repositories.ResponseModel
import com.abstractclass.findmate.repositories.posts.Post
import com.google.gson.annotations.SerializedName

class SearchPostResponseModel: ResponseModel<List<Post>>() {
    @SerializedName("pagination")
    val pagination: Pagination? = null

    data class Pagination(
        @SerializedName("page")
        val page: Int,
        @SerializedName("pagesCount")
        val pagesCount: Int,
        @SerializedName("perPage")
        val perPage: Int,
        @SerializedName("recordsCount")
        val recordsCount: Int)
}