package com.example.myrestaurant.domain.model

data class Dish(
    val id: String,
    val name: String,
    val imageUrl: String,
    val price: Int,
    val rating: Double,
    val cuisineId: String
)
