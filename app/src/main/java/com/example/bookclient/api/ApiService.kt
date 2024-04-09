package com.example.bookclient.api

import retrofit2.http.GET

interface ApiService {
    @GET("/bvaughn/infinite-list-reflow-examples/master/books.json")
    suspend fun getBooks(): List<Book>
}