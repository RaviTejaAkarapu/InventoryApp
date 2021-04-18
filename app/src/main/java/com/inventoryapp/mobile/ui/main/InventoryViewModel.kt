package com.inventoryapp.mobile.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inventoryapp.mobile.entity.Item
import com.inventoryapp.mobile.networkNotification.NetworkMonitor
import com.inventoryapp.mobile.repository.ItemRepository
import com.inventoryapp.mobile.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val itemRepository: ItemRepository,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    private val mutableInventoryAction = MutableLiveData<InventoryAction>()
    private val mutableNetworkStatus = MutableLiveData(false)
    private val mutableExistingItemWithSkuId = MutableLiveData<HashMap<Int, Item?>>()

    val inventoryActionLiveData: LiveData<InventoryAction> = mutableInventoryAction
    val networkStatusLiveData: LiveData<Boolean> = mutableNetworkStatus
    val allItemsLiveData: LiveData<Resource<List<Item>>> = itemRepository.getItems()

    //    val allItemsFromDb: LiveData<List<Item>> = itemRepository.getAllItemsFromDb()
    val existingItemWithSkuId: LiveData<HashMap<Int, Item?>> = mutableExistingItemWithSkuId
    lateinit var selectedItemList: List<Item>

    init {
        viewModelScope.launch { itemRepository.insertDummyItemsList() }
//        viewModelScope.launch { itemRepository.deleteAllItemsFromDB() }
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

    fun setNetworkStatus(isOnline: Boolean) {
        mutableNetworkStatus.postValue(isOnline)
    }

    fun checkForExistingSkuId(skuId: String, currentPosition: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            Result.runCatching {
                itemRepository.getItemsBySkuId(skuId)
            }.onSuccess { item ->
                val map = HashMap<Int, Item?>()
                map[currentPosition] = item
                mutableExistingItemWithSkuId.postValue(map)
            }
        }

    fun getSKUListFromDb(): List<String> {
        val skuList = mutableListOf<String>()
        allItemsLiveData.value?.data?.forEach {
            skuList.add(it.skuId)
        }
        return skuList
    }

    fun getManufacturerListFromDb(): List<String> {
        val manufacturerList = mutableListOf<String>()
        allItemsLiveData.value?.data?.forEach {
            it.manufacturerName?.let { manufacturerName ->
                manufacturerList.add(manufacturerName)
            }
        }
        return manufacturerList
    }

    suspend fun getItemsByQuery(query: String): List<Item>? = withContext(Dispatchers.IO) {
        return@withContext itemRepository.getItemsByQuery(query)
    }

    sealed class InventoryAction {
        object NavigateToUploadInventoryFragment : InventoryAction()
        object NavigateToViewInventoryFragment : InventoryAction()
        object NavigateToSearchInventoryFragment : InventoryAction()
    }
}