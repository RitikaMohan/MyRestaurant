package com.example.myrestaurant.domain.repository

import com.example.myrestaurant.domain.model.CartItem
import com.example.myrestaurant.domain.model.Cuisine
import com.example.myrestaurant.domain.model.Dish

interface FoodRepository {

    suspend fun getItemList(page: Int, count: Int): List<Cuisine>

    suspend fun getItemById(itemId: Int): Dish

    suspend fun getItemByFilter(
        cuisines: List<String>?,
        minPrice: Int?,
        maxPrice: Int?,
        minRating: Float?
    ): List<Cuisine>

    suspend fun makePayment(
        totalAmount: String,
        totalItems: Int,
        items: List<CartItem>
    ): String // txn_ref_no
}
