package com.example.myrestaurant.domain.repository

import com.example.myrestaurant.data.remote.reponseDto.CuisineDto
import com.example.myrestaurant.data.remote.reponseDto.GetItemByIdResponse
import com.example.myrestaurant.data.remote.reponseDto.ItemDto
import com.example.myrestaurant.domain.model.Cuisine
import com.example.myrestaurant.domain.model.Dish

fun CuisineDto.toDomain(): Cuisine {
    return Cuisine(
        id = cuisine_id.toString(),
        name = cuisine_name,
        imageUrl = cuisine_image_url,
        dishes = items.map { it.toDomain(cuisine_id.toString()) }
    )
}

fun ItemDto.toDomain(cuisineId: String): Dish {
    return Dish(
        id = id.toString(),
        name = name,
        imageUrl = image_url,
        price = price?.toIntOrNull() ?: 0,
        rating = rating?.toDoubleOrNull() ?: 0.0,
        cuisineId = cuisineId
    )
}

fun GetItemByIdResponse.toDomain(): Dish {
    return Dish(
        id = item_id.toString(),
        name = item_name,
        imageUrl = item_image_url,
        price = item_price,
        rating = item_rating,
        cuisineId = cuisine_id
    )
}
