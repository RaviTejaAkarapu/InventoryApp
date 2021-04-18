package com.inventoryapp.mobile.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.inventoryapp.mobile.entity.Item

@Dao
interface ItemDao {

    @Insert(onConflict = REPLACE)
    suspend fun insert(item: Item)

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(items: List<Item>)

    @Update
    suspend fun update(item: Item)

    @Delete
    suspend fun delete(item: Item)

    @Query("DELETE FROM Item")
    suspend fun deleteAll()

    @Query("SELECT * FROM Item")
    fun getAllItemsLiveData(): LiveData<List<Item>>

    @Query("SELECT * FROM Item where skuId = :skuId")
    suspend fun getItemsBySkuId(skuId: String): Item?

    @Query("SELECT * FROM Item where itemName LIKE ('%' || :query || '% - %')")
    suspend fun getItemsByQuery(query: String): List<Item>?

    @Query("SELECT * FROM Item where manufacturerName LIKE :manufacturer")
    suspend fun getItemsByManufacturer(manufacturer: String): List<Item>

    @Query("SELECT DISTINCT manufacturerName FROM Item")
    suspend fun getManufacturerList(): List<String>}