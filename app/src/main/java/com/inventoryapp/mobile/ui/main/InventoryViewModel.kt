package com.inventoryapp.mobile.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.inventoryapp.mobile.networkNotification.NetworkMonitor
import com.inventoryapp.mobile.networkNotification.NetworkStatus
import com.inventoryapp.mobile.repository.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val itemRepository: ItemRepository,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    private val mutableInventoryAction = MutableLiveData<InventoryAction>()

    val inventoryActionLiveData: LiveData<InventoryAction> = mutableInventoryAction
    val networkStatusLiveData: LiveData<NetworkStatus> = networkMonitor

    fun getAllItemsLiveData() = itemRepository.getAllItemsLiveData()

    fun navigateToUploadInventoryFragment() {
        mutableInventoryAction.postValue(InventoryAction.NavigateToUploadInventoryFragment)
    }

    fun navigateToViewInventoryFragment() {
        mutableInventoryAction.postValue(InventoryAction.NavigateToViewInventoryFragment)
    }

    sealed class InventoryAction {
        object NavigateToUploadInventoryFragment : InventoryAction()
        object NavigateToViewInventoryFragment : InventoryAction()
    }
}