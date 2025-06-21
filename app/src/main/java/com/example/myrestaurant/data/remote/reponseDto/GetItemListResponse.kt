package com.example.myrestaurant.data.remote.reponseDto

data class GetItemListResponse(
    val response_code: Int,
    val outcome_code: Int,
    val response_message: String,
    val cuisines: List<CuisineDto>
)