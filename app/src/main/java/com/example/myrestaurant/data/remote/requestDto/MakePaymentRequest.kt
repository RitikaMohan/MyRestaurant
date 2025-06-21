package com.example.myrestaurant.data.remote.requestDto

data class MakePaymentRequest(
    val total_amount: String,
    val total_items: Int,
    val data: List<PaymentItem>
)

data class PaymentItem(
    val cuisine_id: Int,
    val item_id: Int,
    val item_price: Int,
    val item_quantity: Int
)
