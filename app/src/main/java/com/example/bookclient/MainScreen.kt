package com.example.bookclient

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.bookclient.api.Book

@Composable
fun MainScreen(
    booksState: BooksState,
    onBookSelected: (Book) -> Unit,
) {
    // A surface container using the 'background' color from the theme
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        when (booksState) {
            is BooksState.Loading -> {
                // Show loading state
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.requiredWidth(64.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                }
            }

            is BooksState.Success -> {
                // Update UI with post data
                val books = booksState.books
                BookList(books = books, onBookSelected = onBookSelected)
            }

            is BooksState.Error -> {
                val errorMessage = booksState.message
                // Handle error
                AlertDialog(
                    title = { Text("Error") },
                    text = { Text(text = errorMessage) },
                    onDismissRequest = { /*TODO*/ },
                    confirmButton = { /*TODO*/ },
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun MainScreenPreviewLoading() {
    MainScreen(booksState = BooksState.Loading) {}
}

@Preview(showSystemUi = true)
@Composable
fun MainScreenPreviewLoaded() {
    val books = mutableListOf<Book>()
    for (i in 1..100) {
        books.add(
            Book(
                "index $i",
                "Title $i",
                thumbnailUrl = "https://m.media-amazon.com/images/I/41WwWFS2s2L.jpg",
                authors = listOf("Author $i"),
                categories = listOf("Category A", "Category B"),
            ),
        )
    }
    MainScreen(booksState = BooksState.Success(books)) {}
}

@Preview(showSystemUi = true)
@Composable
fun MainScreenPreviewError() {
    MainScreen(booksState = BooksState.Error("Network error")) {}
}

@Composable
fun BookList(
    books: List<Book>,
    onBookSelected: (Book) -> Unit,
) {
    LazyColumn {
        items(books) { book ->
            BookItem(book = book, onClicked = onBookSelected)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun BookItem(
    book: Book,
    onClicked: (Book) -> Unit,
) {
    ElevatedCard(
        modifier =
            Modifier
                .padding(8.dp)
                .fillMaxWidth(),
        onClick = { onClicked(book) },
    ) {
        Row {
            GlideImage(
                modifier =
                    Modifier
                        .requiredHeight(124.dp)
                        .widthIn(0.dp, 96.dp),
                model = book.thumbnailUrl,
                contentDescription = null,
                contentScale = ContentScale.FillHeight,
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(modifier = Modifier.padding(4.dp), text = book.title!!)
                Text(modifier = Modifier.padding(4.dp), text = book.authors?.get(0)!!)
                if (!book.categories.isNullOrEmpty()) {
                    Row {
                        for (category in book.categories) {
                            OutlinedCard {
                                Text(modifier = Modifier.padding(4.dp), text = category)
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                    }
                }
            }
        }
    }
}
