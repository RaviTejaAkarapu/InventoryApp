package com.inventoryapp.mobile.service

import com.inventoryapp.mobile.entity.Item
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ItemApi {

    @GET("api/item/getItems")
    suspend fun getAllInventory() : Response<List<Item>>

    @GET("api/item/getItem/{skuId}")
    suspend fun getItem(@Path("skuId") id: String): Response<Item>

}