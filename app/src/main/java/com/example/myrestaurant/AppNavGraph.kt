package com.example.myrestaurant

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myrestaurant.presentation.cart.CartScreen
import com.example.myrestaurant.presentation.cart.CartViewModel
import com.example.myrestaurant.presentation.cuisine.CuisineScreen
import com.example.myrestaurant.presentation.home.HomeScreen

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
    cartViewModel: CartViewModel,
    onLanguageToggle: () -> Unit
)
 {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.HOME
    ) {
        composable(NavRoutes.HOME) {
            HomeScreen(
                cartViewModel = cartViewModel,
                onCuisineClick = { cuisineId ->
                    navController.navigate("cuisine/$cuisineId")
                },
                onCartClick = {
                    navController.navigate(NavRoutes.CART)
                },
                onLanguageToggle = onLanguageToggle
            )
        }

        composable(
            route = NavRoutes.CUISINE_WITH_ID,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val cuisineId = backStackEntry.arguments?.getString("id") ?: ""
            CuisineScreen(
                cuisineId = cuisineId,
                cartViewModel = cartViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(NavRoutes.CART) {
            CartScreen(
                viewModel = cartViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}

object NavRoutes {
    const val HOME = "home"
    const val CUISINE = "cuisine"
    const val CUISINE_WITH_ID = "cuisine/{id}"
    const val CART = "cart"
}
