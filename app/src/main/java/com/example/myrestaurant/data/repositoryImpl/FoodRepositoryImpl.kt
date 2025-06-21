package com.example.myrestaurant.data.repositoryImpl

import com.example.myrestaurant.data.remote.FoodServiceAPI
import com.example.myrestaurant.data.remote.requestDto.GetItemByFilterRequest
import com.example.myrestaurant.data.remote.requestDto.GetItemByIdRequest
import com.example.myrestaurant.data.remote.requestDto.GetItemListRequest
import com.example.myrestaurant.data.remote.requestDto.MakePaymentRequest
import com.example.myrestaurant.data.remote.requestDto.PaymentItem
import com.example.myrestaurant.data.remote.requestDto.PriceRange
import com.example.myrestaurant.domain.model.CartItem
import com.example.myrestaurant.domain.model.Cuisine
import com.example.myrestaurant.domain.model.Dish
import com.example.myrestaurant.domain.repository.FoodRepository
import com.example.myrestaurant.domain.repository.toDomain
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoodRepositoryImpl @Inject constructor(
    private val api: FoodServiceAPI
) : FoodRepository {

    override suspend fun getItemList(page: Int, count: Int): List<Cuisine> {
        val response = api.getItemList("get_item_list", GetItemListRequest(page, count))
        if (response.isSuccessful) {
            return response.body()?.cuisines?.map { it.toDomain() } ?: emptyList()
        } else {
            throw Exception("Failed to fetch items")
        }
    }

    override suspend fun getItemById(itemId: Int): Dish {
        val response = api.getItemById("get_item_by_id", GetItemByIdRequest(itemId))
        if (response.isSuccessful) {
            return response.body()?.toDomain() ?: throw Exception("Empty item")
        } else {
            throw Exception("Failed to fetch item by id")
        }
    }

    override suspend fun getItemByFilter(
        cuisines: List<String>?,
        minPrice: Int?,
        maxPrice: Int?,
        minRating: Float?
    ): List<Cuisine> {
        val priceRange = if (minPrice != null && maxPrice != null) {
            PriceRange(minPrice, maxPrice)
        } else null

        val request = GetItemByFilterRequest(cuisines, priceRange, minRating)
        val response = api.getItemByFilter("get_item_by_filter", request)

        if (response.isSuccessful) {
            return response.body()?.cuisines?.map { it.toDomain() } ?: emptyList()
        } else {
            throw Exception("Failed to filter items")
        }
    }

    override suspend fun makePayment(
        totalAmount: String,
        totalItems: Int,
        items: List<CartItem>
    ): String {
        val paymentItems = items.map {
            PaymentItem(
                cuisine_id = it.dish.id.toInt(), // make sure you have cuisineId in domain model
                item_id = it.dish.id.toInt(),
                item_price = it.dish.price,
                item_quantity = it.quantity
            )
        }

        val response = api.makePayment("make_payment", MakePaymentRequest(totalAmount, totalItems, paymentItems))

        if (response.isSuccessful) {
            return response.body()?.txn_ref_no ?: throw Exception("No transaction reference")
        } else {
            throw Exception("Payment failed")
        }
    }
}
