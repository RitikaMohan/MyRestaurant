package com.example.myrestaurant.data.remote.requestDto

data class GetItemByFilterRequest(
    val cuisine_type: List<String>?,
    val price_range: PriceRange?,
    val min_rating: Float?
)

data class PriceRange(
    val min_amount: Int,
    val max_amount: Int
)