package com.example.bookclient

import androidx.lifecycle.ViewModel
import com.example.bookclient.api.Book
import com.example.bookclient.api.RetrofitClient.apiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _booksStateFlow = MutableStateFlow<BooksState>(BooksState.Loading)
    val booksStateFlow: Flow<BooksState> = _booksStateFlow.asStateFlow()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            getBooks()
        }
    }

    private suspend fun getBooks() {
        try {
            val books = apiService.getBooks()
            val filteredbooks =
                books.filter { !it.isbn.isNullOrEmpty() && !it.title.isNullOrEmpty() && !it.thumbnailUrl.isNullOrEmpty() }
            _booksStateFlow.value = BooksState.Success(filteredbooks)
        } catch (e: Exception) {
            _booksStateFlow.value = BooksState.Error(e.message ?: "")
        }
    }
}

sealed class BooksState {
    data object Loading : BooksState()

    data class Success(
        val books: List<Book>,
    ) : BooksState()

    data class Error(
        val message: String,
    ) : BooksState()
}
