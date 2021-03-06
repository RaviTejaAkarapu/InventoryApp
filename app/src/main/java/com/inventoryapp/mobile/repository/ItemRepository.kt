package com.inventoryapp.mobile.repository

import androidx.lifecycle.LiveData
import com.inventoryapp.mobile.entity.Item
import com.inventoryapp.mobile.util.Resource
import retrofit2.Response

interface ItemRepository {

    suspend fun getAllItemsFromDb(): List<Item>

    fun getAllItemsFromDbLiveData(): LiveData<List<Item>>

    fun getItems(): LiveData<Resource<List<Item>>>

    suspend fun getItemsByQuery(query: String): List<Item>?

    suspend fun getItemsByManufacturer(manufacturer: String): List<Item>

    suspend fun insertDummyItemsList()

    suspend fun getManufacturerList(): List<String>

    suspend fun insertAllItemstoDB(items: List<Item>)

    suspend fun deleteAllItemsFromDB()

    suspend fun getItemsBySkuId(skuId: String): Item?

    suspend fun healthCheck(): Response<Boolean>
}