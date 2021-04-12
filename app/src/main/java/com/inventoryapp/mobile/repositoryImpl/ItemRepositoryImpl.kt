package com.inventoryapp.mobile.repositoryImpl

import androidx.lifecycle.LiveData
import com.inventoryapp.mobile.entity.Item
import com.inventoryapp.mobile.repository.ItemRepository
import com.inventoryapp.mobile.repository.cache.ItemCache

class ItemRepositoryImpl constructor(
    private val itemCache: ItemCache
) : ItemRepository {
    override fun getAllItemsLiveData(): LiveData<List<Item?>?>? =
        itemCache.getAllItemsLiveData()

}