package com.example.myrestaurant.presentation.cuisine

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.myrestaurant.data.remote.NetworkClient
import com.example.myrestaurant.data.repositoryImpl.FoodRepositoryImpl
import com.example.myrestaurant.domain.usecase.GetItemListUseCase
import com.example.myrestaurant.presentation.cart.CartViewModel
import com.example.myrestaurant.presentation.home.DishTile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CuisineScreen(
    cuisineId: String,
    onBackClick: () -> Unit,
    cartViewModel: CartViewModel
) {
    val viewModel = remember {
        val api = NetworkClient.api
        val repo = FoodRepositoryImpl(api)
        val useCase = GetItemListUseCase(repo)
        CuisineViewModel(useCase)
    }

    LaunchedEffect(cuisineId) {
        viewModel.loadCuisine(cuisineId)
    }

    val dishes = viewModel.dishes
    val isLoading = viewModel.isLoading
    val error = viewModel.errorMessage
    val cuisineName = viewModel.cuisineName

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(cuisineName) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6200EE), // Purple background
                    titleContentColor = Color.White     // Optional: white title text
                ),
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        when {
            isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            error != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = error)
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(
                        items = dishes,
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

        }
    }
}
