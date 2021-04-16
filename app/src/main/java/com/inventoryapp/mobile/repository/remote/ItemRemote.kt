package com.inventoryapp.mobile.repository.remote

import com.inventoryapp.mobile.entity.Item
import com.inventoryapp.mobile.util.Resource

interface ItemRemote {
    suspend fun insertItems()

    suspend fun getItems(): Resource<List<Item>>

    suspend fun getItemBySku(id: String): Resource<Item>
}