package com.inventoryapp.mobile.repositoryImpl.cache

import androidx.lifecycle.LiveData
import com.inventoryapp.mobile.dao.ItemDao
import com.inventoryapp.mobile.entity.Item
import com.inventoryapp.mobile.repository.cache.ItemCache

class ItemCacheImpl constructor(
    private val itemDao: ItemDao
) : ItemCache {
    override suspend fun getAllItemsFromDb(): List<Item> {
        return itemDao.getAllItems()
    }

    override suspend fun insertAll(items: List<Item>) {
        itemDao.insertAll(items)
    }

    override suspend fun deleteAll() {
        itemDao.deleteAll()
    }

    override suspend fun getItemsBySkuId(skuId: String): Item? =
        itemDao.getItemsBySkuId(skuId)

    override suspend fun getItemsByQuery(query: String): List<Item>? {
        return itemDao.getItemsByQuery(query)
    }

    override suspend fun getItemsByManufacturer(manufacturer: String): List<Item> {
        return itemDao.getItemsByManufacturer(manufacturer)
    }

    override suspend fun getManufacturerList(): List<String> {
        return itemDao.getManufacturerList()
    }

    override fun getAllItemsFromDbLiveData(): LiveData<List<Item>> {
        return itemDao.getAllItemsLiveData()
    }
}