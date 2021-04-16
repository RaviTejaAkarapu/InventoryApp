package com.inventoryapp.mobile.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inventoryapp.mobile.entity.Item
import com.inventoryapp.mobile.networkNotification.NetworkMonitor
import com.inventoryapp.mobile.networkNotification.NetworkStatus
import com.inventoryapp.mobile.repository.ItemRepository
import com.inventoryapp.mobile.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val itemRepository: ItemRepository,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    private val mutableInventoryAction = MutableLiveData<InventoryAction>()

    val inventoryActionLiveData: LiveData<InventoryAction> = mutableInventoryAction
    val networkStatusLiveData: LiveData<NetworkStatus> = networkMonitor
    val allItemsLiveData: LiveData<Resource<List<Item>>> = itemRepository.getItems()
    val allItemsFromDb: LiveData<List<Item>> = itemRepository.getAllItemsFromDb()
    lateinit var selectedItemList: List<Item>

    init {
//        viewModelScope.launch { itemRepository.insertDummyItemsList() }
        viewModelScope.launch { itemRepository.deleteAllItemsFromDB() }
    }

    fun navigateToUploadInventoryFragment() {
        mutableInventoryAction.postValue(InventoryAction.NavigateToUploadInventoryFragment)
    }

    fun navigateToViewInventoryFragment() {
        mutableInventoryAction.postValue(InventoryAction.NavigateToViewInventoryFragment)
    }

    fun navigateToSearchInventoryFragment() {
        mutableInventoryAction.postValue(InventoryAction.NavigateToSearchInventoryFragment)
    }

    fun insertItemListToDb(items: List<Item>) = viewModelScope.launch {
        itemRepository.insertAllItemstoDB(items)
    }

    fun setSelectedItems(selectedItems: List<Item>) {
        selectedItemList = selectedItems
    }

    sealed class InventoryAction {
        object NavigateToUploadInventoryFragment : InventoryAction()
        object NavigateToViewInventoryFragment : InventoryAction()
        object NavigateToSearchInventoryFragment : InventoryAction()
    }
}