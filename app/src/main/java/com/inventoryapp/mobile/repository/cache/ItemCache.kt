package com.inventoryapp.mobile.repository.cache

import androidx.lifecycle.LiveData
import com.inventoryapp.mobile.entity.Item

interface ItemCache {

    fun getAllItemsLiveData(): LiveData<List<Item>>

    suspend fun insertAll(items: List<Item>)

    suspend fun deleteAll()
    suspend fun getItemsBySkuId(skuId: String): Item?
    suspend fun getItemsByQuery(query: String): List<Item>?
    suspend fun getItemsByManufacturer(manufacturer: String): List<Item>
    suspend fun getManufacturerList(): List<String>
}