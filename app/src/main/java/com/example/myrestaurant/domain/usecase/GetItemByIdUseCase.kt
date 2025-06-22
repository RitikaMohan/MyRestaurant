package com.example.myrestaurant.domain.usecase

import com.example.myrestaurant.domain.model.Dish
import com.example.myrestaurant.domain.repository.FoodRepository
import javax.inject.Inject

class GetItemByIdUseCase @Inject constructor(
    private val repository: FoodRepository
) {
    suspend operator fun invoke(itemId: Int): Dish {
        return repository.getItemById(itemId)
    }
}
