package com.inventoryapp.mobile.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.inventoryapp.mobile.dao.ItemDao
import com.inventoryapp.mobile.entity.Item

@Database(entities = [Item::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun itemDao(): ItemDao
}