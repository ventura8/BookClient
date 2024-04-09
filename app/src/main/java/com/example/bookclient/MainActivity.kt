package com.example.bookclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bookclient.api.Book
import com.example.bookclient.ui.theme.BookClientTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainViewModel: MainViewModel by viewModels()

        setContent {
            BookClientTheme {
                val navController = rememberNavController()
                val booksState by mainViewModel.booksStateFlow.collectAsState(initial = BooksState.Loading)
                var selectedBook by remember {
                    mutableStateOf<Book?>(null)
                }

                NavHost(navController, startDestination = "mainscreen") {
                    composable("mainscreen") {
                        MainScreen(booksState = booksState) {
                            selectedBook = it
                            navController.navigate("bookDetail")
                        }
                    }

                    composable("bookDetail") {
                        DetailScreen(book = selectedBook!!) { navController.popBackStack() }
                    }
                }
            }
        }
    }
}

