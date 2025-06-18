package com.niks.googlebooksapp.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.niks.googlebooksapp.R
import com.niks.googlebooksapp.ui.list.BookDetailsVo
import com.niks.googlebooksapp.ui.list.BooksListingActivityViewModel
import kotlinx.serialization.Serializable

class ComposeActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Google Books") },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                ) { paddingValues ->
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = Route.BooksListingRoute) {
                        composable<Route.BooksListingRoute> {
                            BooksScreen(paddingValues) { bookDetailVo ->
                                navController.navigate(
                                    Route.BooksDetailRoute(
                                    id = bookDetailVo.id,
                                    title = bookDetailVo.title,
                                    publisher = bookDetailVo.publisher,
                                    averageRating = bookDetailVo.averageRating,
                                    pageCount = bookDetailVo.pageCount,
                                    imageUrl = bookDetailVo.imageUrl
                                )
                                )
                            }
                        }
                        composable<Route.BooksDetailRoute> { entry ->
                            val route = entry.toRoute<Route.BooksDetailRoute>()
                            BookDetailsScreen(route, paddingValues, navController)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BookDetailsScreen(bookDetails: Route.BooksDetailRoute, paddingValues: PaddingValues, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Back"
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(bookDetails.imageUrl)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.ic_book_placeholder),
            error = painterResource(R.drawable.ic_broken_image_placeholder),
            contentDescription = bookDetails.title,
            modifier = Modifier
                .height(250.dp)
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            contentScale = ContentScale.Fit
        )

        Text(
            text = bookDetails.title,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (bookDetails.publisher.isNotBlank()) {
            Text(
                text = "Publisher: ${bookDetails.publisher}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }

        Text(
            text = "Rating: ${if (bookDetails.averageRating > 0) bookDetails.averageRating.toString() else "N/A"}",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Text(
            text = "Pages: ${if (bookDetails.pageCount > 0) bookDetails.pageCount.toString() else "N/A"}",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

sealed class Route {
    @Serializable
    data object BooksListingRoute : Route()

    @Serializable
    data class BooksDetailRoute(
        val id: String,
        val title: String,
        val publisher: String,
        val averageRating: Float,
        val pageCount: Int,
        val imageUrl: String
    ) : Route()
}

@Composable
fun BooksScreen(
    paddingValues: PaddingValues,
    booksViewModel: BooksListingActivityViewModel = viewModel(),
    bookItemClicked: (BookDetailsVo) -> Unit
) {
    val uiState by booksViewModel.uiStateFlow.collectAsState()
    val booksList = uiState.booksList
    val isLoading = uiState.isLoading

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (booksList.isEmpty()) {
            Text(
                text = "No books found.",
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.bodyLarge
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(booksList, key = { book -> book.id }) { bookVo ->
                    BookItem(bookDetails = bookVo, bookItemClicked)
                }
            }
        }
    }
}

@Composable
fun BookItem(bookDetails: BookDetailsVo, bookItemClicked: (BookDetailsVo) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                bookItemClicked(bookDetails)
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(bookDetails.imageUrl)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.ic_book_placeholder),
                error = painterResource(R.drawable.ic_broken_image_placeholder),
                contentDescription = bookDetails.title,
                modifier = Modifier
                    .width(80.dp)
                    .height(110.dp)
                    .padding(end = 12.dp),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = bookDetails.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                if (bookDetails.publisher.isNotBlank()) {
                    Text(
                        text = "Publisher: ${bookDetails.publisher}",
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                }
                Text(
                    text = "Rating: ${bookDetails.getRating()}",
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = bookDetails.getPageCountForDetails(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}