package com.example.myrestaurant.domain.usecase

import com.example.myrestaurant.domain.model.CartItem
import com.example.myrestaurant.domain.repository.FoodRepository
import javax.inject.Inject

class MakePaymentUseCase @Inject constructor(
    private val repository: FoodRepository
) {
    suspend operator fun invoke(
        totalAmount: String,
        totalItems: Int,
        items: List<CartItem>
    ): String {
        return repository.makePayment(totalAmount, totalItems, items)
    }
}
