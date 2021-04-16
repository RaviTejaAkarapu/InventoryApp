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
    override fun getAllItemsFromDb(): LiveData<List<Item>> {
        return itemCache.getAllItemsLiveData()
    }

    override suspend fun insertDummyItemsList() {
        itemRemote.insertItems()
    }

    override suspend fun deleteAllItemsFromDB() {
        itemCache.deleteAll()
    }

    override fun getItems() = performGetOperation(
        databaseQuery = { itemCache.getAllItemsLiveData() },
        networkCall = { itemRemote.getItems() },
        saveCallResult = { itemCache.insertAll(it) }
    )

    override suspend fun insertAllItemstoDB(items: List<Item>) {
        itemCache.insertAll(items)
    }

}