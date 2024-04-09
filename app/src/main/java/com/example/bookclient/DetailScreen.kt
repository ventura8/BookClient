package com.example.bookclient

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.bookclient.api.Book
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    book: Book,
    onGoBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopAppBar(
                colors =
                    topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                title = {
                    Text(book.title!!)
                },
                navigationIcon = {
                    IconButton(onClick = onGoBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Localized description",
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                scope.launch {
                    snackbarHostState.showSnackbar("You are about to buy the book")
                }
            }) {
                Icon(Icons.Default.ShoppingCart, contentDescription = "Buy")
            }
        },
    ) { innerPadding ->
        Surface(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
        ) {
            Column(
                modifier =
                    Modifier
                        .padding(8.dp)
                        .verticalScroll(rememberScrollState()),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier =
                        Modifier
                            .height(IntrinsicSize.Min)
                            .fillMaxWidth(),
                ) {
                    ElevatedCard(
                        Modifier
                            .requiredWidth(96.dp)
                            .requiredHeight(128.dp),
                    ) {
                        GlideImage(
                            modifier = Modifier.fillMaxWidth(),
                            model = book.thumbnailUrl,
                            contentDescription = null,
                            contentScale = ContentScale.FillBounds,
                        )
                    }

                    Box(
                        modifier =
                            Modifier
                                .padding(8.dp)
                                .fillMaxSize(),
                    ) {
                        Column(Modifier.align(Alignment.TopStart)) {
                            for (author in book.authors!!) {
                                Text(text = author)
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                        }

                        if (!book.publishedDate?.date.isNullOrEmpty()) {
                            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                            val date = dateFormat.parse(book.publishedDate!!.date)
                            val output: String =
                                SimpleDateFormat("dd MM yyyy").format(date)
                            Text(
                                modifier = Modifier.align(Alignment.BottomEnd),
                                text = output,
                            )
                        }
                    }
                }

                Row {
                    for (cat in book.categories!!) {
                        OutlinedCard(modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)) {
                            Text(text = cat, modifier = Modifier.padding(4.dp))
                        }
                    }
                }
                if (!book.longDescription.isNullOrEmpty()) {
                    Text(text = book.longDescription)
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun DetailScreenPreview() {
    DetailScreen(
        book =
            Book(
                title = "Title",
                longDescription = "longDescription",
                authors = listOf("Author 1", "Author 2"),
                categories = listOf("Category 1", "Category 2"),
            ),
        onGoBack = {},
    )
}
