package com.inventoryapp.mobile.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.inventoryapp.mobile.databinding.FragmentUploadInventoryBinding
import com.inventoryapp.mobile.entity.Item
import com.inventoryapp.mobile.util.observeForChange
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UploadInventoryFragment : Fragment(), AddItemAdapter.AddItemActionListener {

    private val viewModel by activityViewModels<InventoryViewModel>()
    private lateinit var addItemAdapter: AddItemAdapter
    private var _binding: FragmentUploadInventoryBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUploadInventoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setView()
        setListeners()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.networkStatusLiveData.observe(viewLifecycleOwner) { networkStatus ->
            networkStatus?.let {
                setNetworkStatusView(isOnline = it.body() == true)
            } ?: setNetworkStatusView(isOnline = false)
        }

        viewModel.existingItemWithSkuId.observeForChange(viewLifecycleOwner) { map ->
            map.forEach { (position, item) ->
                item?.let {
                    addItemAdapter.updateItem(item, position)
                }
            }
        }
    }

    private fun setNetworkStatusView(isOnline: Boolean) {
        binding.run {
            networkStatusOffline.isVisible = !isOnline
            networkStatusOnline.isVisible = isOnline
        }
    }

    private fun setListeners() {
        binding.apply {
            saveButton.setOnClickListener {
                val itemList = addItemAdapter.itemList.filter { item -> item.skuId.isNotEmpty() }
                if (itemList.any { viewModel.getSKUListFromDb().contains(it.skuId).not() })
                    AlertDialog.Builder(requireContext())
                        .setTitle("This list has unidentified SKUs")
                        .setMessage("Please click OK to save only Identified SKUs")
                        .setPositiveButton("YES") { _, _ ->
                            viewModel.insertItemListToDb(
                                itemList.filter { item ->
                                    viewModel.getSKUListFromDb().contains(item.skuId)
                                }
                            )
                            viewModel.navigateToViewInventoryFragment()
                        }
                        .setNegativeButton("NO", null)
                        .create()
                        .show()
                else {
                    viewModel.insertItemListToDb(
                        itemList.filter { item ->
                            viewModel.getSKUListFromDb().contains(item.skuId)
                        }
                    )
                    viewModel.navigateToViewInventoryFragment()
                }
            }
            searchButton.setOnClickListener {
                viewModel.navigateToSearchInventoryFragment()
            }
        }
    }

    private fun setView() {
        binding.addItemRecyclerView.apply {
            addItemAdapter = AddItemAdapter(this@UploadInventoryFragment)
            adapter = addItemAdapter
        }

        viewModel.selectedItemList.let {
            addItemAdapter.addItems(
                if (it.isNotEmpty())
                    it as ArrayList<Item>
                else
                    arrayListOf(Item("", "", ""))
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun checkForExistingSkuId(skuId: String, currentPosition: Int) {
        viewModel.checkForExistingSkuId(skuId, currentPosition)
    }
}