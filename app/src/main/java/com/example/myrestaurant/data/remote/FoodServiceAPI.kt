package com.example.myrestaurant.data.remote

import com.example.myrestaurant.data.remote.reponseDto.GetItemByFilterResponse
import com.example.myrestaurant.data.remote.reponseDto.GetItemByIdResponse
import com.example.myrestaurant.data.remote.reponseDto.GetItemListResponse
import com.example.myrestaurant.data.remote.reponseDto.MakePaymentResponse
import com.example.myrestaurant.data.remote.requestDto.GetItemByFilterRequest
import com.example.myrestaurant.data.remote.requestDto.GetItemByIdRequest
import com.example.myrestaurant.data.remote.requestDto.GetItemListRequest
import com.example.myrestaurant.data.remote.requestDto.MakePaymentRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface FoodServiceAPI {
    @POST("emulator/interview/get_item_list")
    suspend fun getItemList(
        @Header("X-Forward-Proxy-Action") action: String = "get_item_list",
        @Body body: GetItemListRequest
    ): Response<GetItemListResponse>

    @POST("emulator/interview/get_item_by_id")
    suspend fun getItemById(
        @Header("X-Forward-Proxy-Action") action: String = "get_item_by_id",
        @Body body: GetItemByIdRequest
    ): Response<GetItemByIdResponse>

    @POST("emulator/interview/get_item_by_filter")
    suspend fun getItemByFilter(
        @Header("X-Forward-Proxy-Action") action: String = "get_item_by_filter",
        @Body body: GetItemByFilterRequest
    ): Response<GetItemByFilterResponse>

    @POST("emulator/interview/make_payment")
    suspend fun makePayment(
        @Header("X-Forward-Proxy-Action") action: String = "make_payment",
        @Body body: MakePaymentRequest
    ): Response<MakePaymentResponse>
}