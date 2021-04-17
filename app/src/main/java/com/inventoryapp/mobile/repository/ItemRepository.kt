package com.inventoryapp.mobile.repository

import androidx.lifecycle.LiveData
import com.inventoryapp.mobile.entity.Item
import com.inventoryapp.mobile.util.Resource

interface ItemRepository {

    fun getAllItemsFromDb(): LiveData<List<Item>>

    suspend fun insertDummyItemsList()

    fun getItems(): LiveData<Resource<List<Item>>>

    suspend fun insertAllItemstoDB(items: List<Item>)

    suspend fun deleteAllItemsFromDB()

    suspend fun getItemsBySkuId(skuId: String): Item
}