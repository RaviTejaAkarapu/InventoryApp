package com.inventoryapp.mobile.repositoryImpl

import androidx.lifecycle.LiveData
import com.inventoryapp.mobile.entity.Item
import com.inventoryapp.mobile.repository.ItemRepository
import com.inventoryapp.mobile.repository.cache.ItemCache
import com.inventoryapp.mobile.repository.remote.ItemRemote
import javax.inject.Inject

class ItemRepositoryImpl @Inject constructor(
    private val itemCache: ItemCache,
    private val itemRemote: ItemRemote
) : ItemRepository {
    override fun getAllItemsLiveData(): LiveData<List<Item?>?> =
        itemCache.getAllItemsLiveData()

}