package com.example.myrestaurant.data.remote.reponseDto

data class MakePaymentResponse(
    val response_code: Int,
    val outcome_code: Int,
    val response_message: String,
    val txn_ref_no: String
)

