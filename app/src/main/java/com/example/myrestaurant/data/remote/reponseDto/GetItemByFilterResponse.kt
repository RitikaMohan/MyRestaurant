package com.example.myrestaurant.data.remote.reponseDto

data class GetItemByFilterResponse(
    val response_code: Int,
    val outcome_code: Int,
    val response_message: String,
    val cuisines: List<CuisineDto>
)

data class CuisineDto(
    val cuisine_id: Int,
    val cuisine_name: String,
    val cuisine_image_url: String,
    val items: List<ItemDto>
)

data class ItemDto(
    val id: Int,
    val name: String,
    val image_url: String,
    val price: String? = null,
    val rating: String? = null
)

