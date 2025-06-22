package com.example.myrestaurant.domain.usecase

import com.example.myrestaurant.domain.model.Cuisine
import com.example.myrestaurant.domain.repository.FoodRepository
import javax.inject.Inject

class GetItemListUseCase @Inject constructor(
    private val repository: FoodRepository
) {
    suspend operator fun invoke(page: Int, count: Int): List<Cuisine> {
        return repository.getItemList(page, count)
    }
}
