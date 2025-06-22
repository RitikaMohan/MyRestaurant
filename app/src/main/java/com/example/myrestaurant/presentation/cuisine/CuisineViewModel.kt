package com.example.myrestaurant.presentation.cuisine

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myrestaurant.domain.model.Dish
import com.example.myrestaurant.domain.usecase.GetItemListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class CuisineViewModel(
    private val getItemListUseCase: GetItemListUseCase
) : ViewModel() {

    var dishes by mutableStateOf<List<Dish>>(emptyList())
        private set

    var cuisineName by mutableStateOf("")
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun loadCuisine(cuisineId: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                val cuisines = getItemListUseCase(1, 10)
                val cuisine = cuisines.find { it.id == cuisineId }
                cuisineName = cuisine?.name ?: "Unknown Cuisine"
                dishes = cuisine?.dishes ?: emptyList()
            } catch (e: Exception) {
                errorMessage = e.localizedMessage
            } finally {
                isLoading = false
            }
        }
    }
}
