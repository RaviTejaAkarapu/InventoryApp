package com.inventoryapp.mobile.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.inventoryapp.mobile.entity.Item

@Dao
interface ItemDao {

    @Insert(onConflict = REPLACE)
    fun insert(item: Item)

    @Update
    fun update(item: Item)

    @Delete
    fun delete(item: Item)

    @Query("SELECT * FROM Item")
    fun getAllItemsLiveData(): LiveData<List<Item?>?>
}