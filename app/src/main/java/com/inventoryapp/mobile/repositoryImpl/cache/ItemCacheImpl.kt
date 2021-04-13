package com.inventoryapp.mobile.repositoryImpl.cache

import androidx.lifecycle.LiveData
import com.inventoryapp.mobile.dao.ItemDao
import com.inventoryapp.mobile.entity.Item
import com.inventoryapp.mobile.repository.cache.ItemCache

class ItemCacheImpl constructor(
    private val itemDao: ItemDao
) : ItemCache {
    override fun getAllItemsLiveData(): LiveData<List<Item>> {

        return itemDao.getAllItemsLiveData()
    }

    override suspend fun insertAll(items: List<Item>) {
        itemDao.insertAll(items)
    }

}