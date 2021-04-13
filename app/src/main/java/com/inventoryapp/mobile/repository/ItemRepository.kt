package com.inventoryapp.mobile.repository

import androidx.lifecycle.LiveData
import com.inventoryapp.mobile.entity.Item

interface ItemRepository {

    fun getAllItemsLiveData(): LiveData<List<Item?>?>

}