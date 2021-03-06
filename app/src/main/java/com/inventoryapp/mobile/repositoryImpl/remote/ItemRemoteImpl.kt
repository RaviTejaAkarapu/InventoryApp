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
        items.add(Item("ABC001", "Lays - 100gms", "Fiato", 4))
        items.add(Item("ABC002", "Measuring Tape - 1m", "Gripper Co.", 4))
        items.add(Item("ABC003", "Coke - 500ml", "Coca Cola Co."))
        items.add(Item("ABC004", "Cookies - 100gms", "Good day"))
        items.add(Item("ABC005", "Bingo - 200gms", "Fiato", 4))
        items.add(Item("ABC006", "Lyzol Cleaner - 2.5Ltr", "Lyzol", 4))
        items.add(Item("ABC007", "Pepsi - 500ml", "Coca cola"))
        items.add(Item("ABC008", "Maggi - 250gms", "Nestle"))
        items.add(Item("ABC009", "Bru Coffee - 100gms", "Nestle"))
        items.add(Item("ABC008", "Tetley Green Tea - 250gms", "Tata Products"))
        itemDao.insertAll(items)
    }

    override suspend fun getItems() =
        getResult {
            itemApi.getAllInventory()
        }

    override suspend fun getItemBySku(id: String) =
        getResult { itemApi.getItem(id) }

    override suspend fun healthCheck() =
        itemApi.healthCheck()
}