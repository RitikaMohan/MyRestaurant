package com.example.myrestaurant.presentation.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myrestaurant.domain.model.Cuisine
import com.example.myrestaurant.domain.usecase.GetItemListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel(
    private val getItemListUseCase: GetItemListUseCase
) : ViewModel() {

    var cuisines by mutableStateOf<List<Cuisine>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        fetchCuisines()
    }

    private fun fetchCuisines() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                cuisines = getItemListUseCase(page = 1, count = 10)
            } catch (e: Exception) {
                errorMessage = e.localizedMessage ?: "Unknown error"
            }
            isLoading = false
        }
    }
}
