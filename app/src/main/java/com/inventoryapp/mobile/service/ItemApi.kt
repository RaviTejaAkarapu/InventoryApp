package com.inventoryapp.mobile.service

import com.inventoryapp.mobile.entity.Item
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ItemApi {

    @GET("itemInventory")
    suspend fun getAllInventory() : Response<List<Item>>

    @GET("itemInventory/{skuId}")
    suspend fun getItem(@Path("id") id: String): Response<Item>

}