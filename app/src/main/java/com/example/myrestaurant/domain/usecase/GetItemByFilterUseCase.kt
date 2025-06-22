package com.example.myrestaurant.domain.usecase

import com.example.myrestaurant.domain.model.Cuisine
import com.example.myrestaurant.domain.repository.FoodRepository
import javax.inject.Inject

class GetItemByFilterUseCase @Inject constructor(
    private val repository: FoodRepository
) {
    suspend operator fun invoke(
        cuisines: List<String>?,
        minPrice: Int?,
        maxPrice: Int?,
        minRating: Float?
    ): List<Cuisine> {
        return repository.getItemByFilter(cuisines, minPrice, maxPrice, minRating)
    }
}
