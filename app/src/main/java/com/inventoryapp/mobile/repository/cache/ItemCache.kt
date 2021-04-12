package com.inventoryapp.mobile.repository.cache

import androidx.lifecycle.LiveData
import com.inventoryapp.mobile.entity.Item

interface ItemCache {

    fun getAllItemsLiveData(): LiveData<List<Item?>?>?

}