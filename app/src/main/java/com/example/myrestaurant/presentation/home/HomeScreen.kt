package com.example.myrestaurant.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.myrestaurant.R
import com.example.myrestaurant.data.remote.NetworkClient
import com.example.myrestaurant.data.repositoryImpl.FoodRepositoryImpl
import com.example.myrestaurant.domain.model.Cuisine
import com.example.myrestaurant.domain.model.Dish
import com.example.myrestaurant.domain.usecase.GetItemListUseCase
import com.example.myrestaurant.presentation.cart.CartViewModel
import kotlin.random.Random


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    cartViewModel: CartViewModel,
    onCuisineClick: (String) -> Unit,
    onCartClick: () -> Unit,
    onLanguageToggle: () -> Unit
) {
    // Manual DI
    val viewModel = remember {
        val api = NetworkClient.api
        val repo = FoodRepositoryImpl(api)
        val useCase = GetItemListUseCase(repo)
        HomeViewModel(useCase)
    }
    val cuisines = viewModel.cuisines
    val isLoading = viewModel.isLoading
    val errorMessage = viewModel.errorMessage
    val loopedList = remember(cuisines) {
        List(100) { cuisines }.flatten()
    }

    val scrollState = rememberLazyListState()

    LaunchedEffect(Unit) {
        scrollState.scrollToItem(loopedList.size / 2)
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.home)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6200EE), // Purple background
                    titleContentColor = Color.White     // Optional: white title text
                ),
                actions = {
                    IconButton(onClick = onLanguageToggle) {
                        Icon(Icons.Default.Translate, contentDescription = "Language", tint = Color.White)
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when {
                isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
                errorMessage != null -> Text(
                    text = errorMessage,
                    modifier = Modifier.align(Alignment.Center)
                )

                else -> {
                    Column(modifier= Modifier.fillMaxSize()) {
                        // Segment 1: Cuisine Horizontal Scroll
                        LazyRow(
                            state = scrollState,
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            flingBehavior = rememberSnapFlingBehavior(lazyListState = rememberLazyListState())
                        ) {
                            items(loopedList, key = { it.id + Random.nextInt() }) { cuisine ->
                                CuisineCard(
                                    cuisine = cuisine,
                                    modifier = Modifier
                                        .width(320.dp) // single item visible
                                        .height(160.dp),
                                    onClick = { onCuisineClick(cuisine.id) }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Top 3 Dishes (Segment 2)
                        val topDishes =
                            cuisines.flatMap { it.dishes }.sortedByDescending { it.rating }.take(10)
                        Text(
                            text = stringResource(R.string.topDishes),
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(8.dp)
                        )

                        LazyColumn(
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(
                                items = topDishes,
                                key = { it.id },
                                itemContent = { dish ->
                                    DishTile(
                                        dish = dish,
                                        quantity = cartViewModel.getQuantity(dish.id),
                                        onAddClick = { cartViewModel.addToCart(dish) },
                                        onRemoveClick = { cartViewModel.removeFromCart(dish) }
                                    )
                                }
                            )
                        }
                    }

                    // Cart Button (Segment 3)
                    FloatingActionButton(
                        onClick = onCartClick,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp)
                    ) {
                        Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "Cart")
                    }
                }
            }
        }
    }
}

@Composable
fun CuisineCard(
    cuisine: Cuisine,
    modifier: Modifier = Modifier, // ✅ Add this
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp)
    ) {
        Box {
            AsyncImage(
                model = cuisine.imageUrl,
                contentDescription = cuisine.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Text(
                text = cuisine.name,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp),
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}



@Composable
fun DishTile(
    dish: Dish,
    onAddClick: () -> Unit,
    onRemoveClick: () -> Unit,
    quantity: Int = 0
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = dish.imageUrl,
                contentDescription = dish.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(dish.name, style = MaterialTheme.typography.titleMedium)
                Text("₹${dish.price} • ⭐ ${dish.rating}")
            }

            // Counter is always visible, even if quantity is 0 ✅
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onRemoveClick,
                    enabled = quantity > 0 // optionally disable at 0
                ) {
                    Icon(Icons.Default.Remove, contentDescription = "Remove")
                }

                Text(
                    text = quantity.toString(), // always shown ✅
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.width(24.dp),
                    textAlign = TextAlign.Center
                )

                IconButton(onClick = onAddClick) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        }
    }
}

