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

    @Query("SELECT * FROM Item")
    fun getAllItemsLiveData(): LiveData<List<Item>>
}