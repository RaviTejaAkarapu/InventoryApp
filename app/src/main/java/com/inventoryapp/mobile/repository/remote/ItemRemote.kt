package com.inventoryapp.mobile.repository.remote

import androidx.lifecycle.LiveData
import com.inventoryapp.mobile.entity.Item
import com.inventoryapp.mobile.util.Resource
import retrofit2.Response

interface ItemRemote {
    suspend fun insertItems()

    suspend fun getItems(): Resource<List<Item>>

    suspend fun getItemBySku(id: String): Resource<Item>

    suspend fun healthCheck(): Response<Boolean>
}