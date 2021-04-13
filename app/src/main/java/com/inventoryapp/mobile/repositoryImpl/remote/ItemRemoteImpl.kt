package com.inventoryapp.mobile.repositoryImpl.remote

import com.inventoryapp.mobile.dao.ItemDao
import com.inventoryapp.mobile.entity.Item
import com.inventoryapp.mobile.repository.remote.ItemRemote
import com.inventoryapp.mobile.service.ItemApi
import com.inventoryapp.mobile.util.RemoteResponseUtil

class ItemRemoteImpl constructor(
    private val itemDao: ItemDao,
    private val itemApi: ItemApi
) : ItemRemote, RemoteResponseUtil() {

    override suspend fun insertItems() {
        val items = mutableListOf<Item>()
        items.add(Item("sbbd7", "Lays - 30", "Pepsi", 4))
        items.add(Item("sfbd7", "Lays - 50", "Pepsi", 4))
        items.add(Item("sbasfd7", "Lays - 32", "Pepsi"))
        items.add(Item("sbbdss7", "Lays - 10", "Pepsi"))
        items.add(Item("sbff4d7", "Lays - 40", "Pepsi", 4))
        itemDao.insertAll(items)
    }

    override suspend fun getItems() =
        getResult { itemApi.getAllInventory() }

    override suspend fun getItemBySku(id: String) =
        getResult { itemApi.getItem(id) }
}