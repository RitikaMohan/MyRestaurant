package com.example.myrestaurant

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.myrestaurant.data.remote.NetworkClient
import com.example.myrestaurant.data.repositoryImpl.FoodRepositoryImpl
import com.example.myrestaurant.domain.usecase.MakePaymentUseCase
import com.example.myrestaurant.presentation.cart.CartViewModel
import com.example.myrestaurant.utils.switchLanguage
import dagger.hilt.android.HiltAndroidApp
import java.util.Locale

@Composable
fun RestaurantApp() {
    val context = LocalContext.current
    val cartViewModel = remember {
        val api = NetworkClient.api
        val repo = FoodRepositoryImpl(api)
        val useCase = MakePaymentUseCase(repo)
        CartViewModel(useCase)
    }

    AppNavGraph(cartViewModel = cartViewModel,
        onLanguageToggle = {
            val currentLang = Locale.getDefault().language
            val newLang = if (currentLang == "en") "hi" else "en"
            switchLanguage(context, newLang)
        })
}