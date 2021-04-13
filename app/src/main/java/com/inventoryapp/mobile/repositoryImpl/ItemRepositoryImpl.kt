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
    override fun getAllItemsLiveData(): LiveData<List<Item>> {
        return itemCache.getAllItemsLiveData()
    }

    override suspend fun insertDummyItemsList() {
        itemRemote.insertItems()
    }

//    fun getCharacter(id: Int) = performGetOperation(
//        databaseQuery = { localDataSource.getCharacter(id) },
//        networkCall = { remoteDataSource.getCharacter(id) },
//        saveCallResult = { localDataSource.insert(it) }
//    )

    override fun getItems() = performGetOperation(
        databaseQuery = { itemCache.getAllItemsLiveData() },
        networkCall = { itemRemote.getItems() },
        saveCallResult = { itemCache.insertAll(it) }
    )

}