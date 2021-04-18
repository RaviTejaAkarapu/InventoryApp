package com.inventoryapp.mobile.repositoryImpl

import androidx.lifecycle.LiveData
import com.inventoryapp.mobile.entity.Item
import com.inventoryapp.mobile.repository.ItemRepository
import com.inventoryapp.mobile.repository.cache.ItemCache
import com.inventoryapp.mobile.repository.remote.ItemRemote
import com.inventoryapp.mobile.util.performGetOperation
import javax.inject.Inject

class ItemRepositoryImpl @Inject constructor(
    private val itemCache: ItemCache,
    private val itemRemote: ItemRemote
) : ItemRepository {
    override suspend fun getAllItemsFromDb(): List<Item> {
        return itemCache.getAllItemsFromDb()
    }
    override fun getAllItemsFromDbLiveData(): LiveData<List<Item>> {
        return itemCache.getAllItemsFromDbLiveData()
    }

    override suspend fun getItemsByQuery(query: String): List<Item>? {
        return itemCache.getItemsByQuery(query)
    }

    override suspend fun getItemsByManufacturer(manufacturer: String): List<Item> {
        return itemCache.getItemsByManufacturer(manufacturer)
    }

    override suspend fun insertDummyItemsList() {
        itemRemote.insertItems()
    }

    override suspend fun getManufacturerList(): List<String> {
        return itemCache.getManufacturerList()
    }

    override suspend fun deleteAllItemsFromDB() {
        itemCache.deleteAll()
    }

    override fun getItems() = performGetOperation(
        databaseQuery = { itemCache.getAllItemsFromDbLiveData() },
        networkCall = { itemRemote.getItems() },
        saveCallResult = { itemCache.insertAll(it) }
    )

    override suspend fun getItemsBySkuId(skuId: String): Item? =
        itemCache.getItemsBySkuId(skuId)

    override suspend fun insertAllItemstoDB(items: List<Item>) {
        itemCache.insertAll(items)
    }

}