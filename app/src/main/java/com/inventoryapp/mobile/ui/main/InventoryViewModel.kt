package com.inventoryapp.mobile.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inventoryapp.mobile.entity.Item
import com.inventoryapp.mobile.repository.ItemRepository
import com.inventoryapp.mobile.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val itemRepository: ItemRepository
) : ViewModel() {

    private val mutableInventoryAction = MutableLiveData<InventoryAction>()
    private var mutableNetworkStatus = MutableLiveData<Response<Boolean>?>()
    private val mutableExistingItemWithSkuId = MutableLiveData<HashMap<Int, Item?>>()
    private val mutableGetAllItemsFromDb = MutableLiveData<List<Item>>()

    val inventoryActionLiveData: LiveData<InventoryAction> = mutableInventoryAction
    val networkStatusLiveData: LiveData<Response<Boolean>?> = mutableNetworkStatus
    val allItemsLiveData: LiveData<Resource<List<Item>>> = itemRepository.getItems()

    val allItemsFromDb: LiveData<List<Item>> = mutableGetAllItemsFromDb
    val existingItemWithSkuId: LiveData<HashMap<Int, Item?>> = mutableExistingItemWithSkuId
    lateinit var selectedItemList: List<Item>
    lateinit var currentItemList: List<Item>

    init {
        viewModelScope.launch { itemRepository.insertDummyItemsList() }
//        viewModelScope.launch { itemRepository.deleteAllItemsFromDB() }
    }

    fun getAllItemsFromDb() =
        viewModelScope.launch(Dispatchers.IO) {
            Result.runCatching {
                itemRepository.getAllItemsFromDb()
            }.onSuccess { itemList ->
                mutableGetAllItemsFromDb.postValue(itemList)
            }
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

    @OptIn(InternalCoroutinesApi::class)
    fun setNetworkStatus() {
        val interval = 3000L
        viewModelScope.launch(Dispatchers.Default) {
            while (NonCancellable.isActive) {
                Result.runCatching {
                    itemRepository.healthCheck()
                }.onSuccess {
                    mutableNetworkStatus.postValue(it)
                }.onFailure {
                    mutableNetworkStatus.postValue(null)
                }
                delay(interval)
            }
        }
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
        allItemsFromDb.value?.forEach {
            skuList.add(it.skuId)
        }
        return skuList
    }

    suspend fun getManufacturerListFromDb(): List<String> {
        return itemRepository.getManufacturerList()
    }

    suspend fun getItemsByQuery(query: String): List<Item>? = withContext(Dispatchers.IO) {
        return@withContext itemRepository.getItemsByQuery(query)
    }

    suspend fun getItemsByManufacturer(manufacturer: String): List<Item> =
        withContext(Dispatchers.IO) {
            return@withContext itemRepository.getItemsByManufacturer(manufacturer)
        }

    sealed class InventoryAction {
        object NavigateToUploadInventoryFragment : InventoryAction()
        object NavigateToViewInventoryFragment : InventoryAction()
        object NavigateToSearchInventoryFragment : InventoryAction()
    }
}