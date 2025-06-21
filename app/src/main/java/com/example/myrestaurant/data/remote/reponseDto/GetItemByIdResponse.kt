package com.example.myrestaurant.data.remote.reponseDto

data class GetItemByIdResponse(
    val response_code: Int,
    val outcome_code: Int,
    val response_message: String,
    val cuisine_id: String,
    val cuisine_name: String,
    val cuisine_image_url: String,
    val item_id: Int,
    val item_name: String,
    val item_price: Int,
    val item_rating: Double,
    val item_image_url: String
)

