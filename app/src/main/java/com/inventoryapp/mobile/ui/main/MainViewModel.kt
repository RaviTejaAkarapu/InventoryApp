package com.inventoryapp.mobile.ui.main

import androidx.lifecycle.ViewModel
import com.inventoryapp.mobile.repository.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val itemRepository: ItemRepository
) : ViewModel() {
//    fun getAllItemsLiveData() = itemRepository.getAllItemsLiveData()
}