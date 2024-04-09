package com.example.bookclient.api

import com.google.gson.annotations.SerializedName

data class Book(
    val isbn: String? = null,
    val title: String? = null,
    val thumbnailUrl: String? = null,
    val shortDescription: String? = null,
    val longDescription: String? = null,
    val authors: List<String>? = null,
    val categories: List<String>? = null,
    val pageCount: Int? = null,
    val publishedDate: PublishedDate? = null,
)

data class PublishedDate(
    @SerializedName("\$date")
    val date: String,
)
