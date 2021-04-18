package com.inventoryapp.mobile.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.inventoryapp.mobile.R
import com.inventoryapp.mobile.databinding.FragmentViewInventoryBinding
import com.inventoryapp.mobile.entity.Item
import com.inventoryapp.mobile.entity.SelectableItem
import com.inventoryapp.mobile.util.AfterTextChanged
import com.inventoryapp.mobile.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ViewInventoryFragment : Fragment(), ItemListAdapter.ItemActionListener {

    private var _binding: FragmentViewInventoryBinding? = null

    private val binding
        get() = _binding!!
    private val viewModel by activityViewModels<InventoryViewModel>()

    private lateinit var itemListAdapter: ItemListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewInventoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setView()
        setListeners()
        observeViewModel()
    }

    private fun setView() {
        binding.itemRecyclerView.apply {
            itemListAdapter = ItemListAdapter(this@ViewInventoryFragment)
            adapter = itemListAdapter
        }
        setEditButtonClickable()

        binding.itemSearchBox.addTextChangedListener(textWatcher)
        binding.manufacturerDropdownBox.apply {
            val manufacturerList: MutableList<String> = mutableListOf("Select")
            lifecycleScope.launch {
                manufacturerList.addAll(viewModel.getManufacturerListFromDb())
            }.invokeOnCompletion {
                adapter = ArrayAdapter(
                    requireContext(),
                    R.layout.view_holder_spinner_unit,
                    manufacturerList
                )
                onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        if (position == 0) {
                            viewModel.allItemsLiveData.value?.data?.let {
                                itemListAdapter.setItems(it.map { item ->
                                    SelectableItem(item, isSelected = false)
                                } as ArrayList<SelectableItem>)
                            }
                        } else {
                            val list = mutableListOf<SelectableItem>()
                            list.addAll(itemListAdapter.itemList.filter { selectableItem ->
                                selectableItem.item.manufacturerName.equals(manufacturerList[position])
                            })
                            itemListAdapter.setItems(
                                list as ArrayList<SelectableItem>
                            )
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        observeViewModel()
    }

    private val textWatcher = AfterTextChanged {
        var list = mutableListOf<Item>()
        lifecycleScope.launch {
            list = viewModel.getItemsByQuery(it.toString()) as MutableList<Item>
        }.invokeOnCompletion {
            itemListAdapter.setItems(list.map { item ->
                SelectableItem(
                    item,
                    false
                )
            } as ArrayList<SelectableItem>)
        }
    }

    override fun setEditButtonClickable() {
        binding.apply {
            addOrEditButton.isClickable =
                itemListAdapter.getSelectedItems().isNotEmpty()
        }
    }

    private fun observeViewModel() {
        viewModel.networkStatusLiveData.observe(viewLifecycleOwner) { networkStatus ->
            when (networkStatus) {
                false -> setNetworkStatusView(isOnline = false)
                true -> setNetworkStatusView(isOnline = true)
            }
        }

        viewModel.allItemsLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                Resource.Status.Success -> {
                    binding.progressBar.isVisible = false
                    itemListAdapter.setItems(
                        ArrayList(it.data?.map { item ->
                            SelectableItem(item, isSelected = false)
                        })
                    )
                    handleItemListView(hasItems = !it.data.isNullOrEmpty())
                    viewModel.setNetworkStatus(isOnline = true)
                }
                Resource.Status.Error -> {
                    binding.progressBar.isVisible = false
                    Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
                    viewModel.setNetworkStatus(isOnline = false)
                }
                Resource.Status.Loading -> {
                    binding.emptyInventoryView.isVisible = false
                    binding.progressBar.isVisible = true

                }
            }
        }
    }

    private fun handleItemListView(hasItems: Boolean) {
        binding.run {
            emptyInventoryView.isVisible = !hasItems
            itemListView.isVisible = hasItems
        }
    }

    private fun setNetworkStatusView(isOnline: Boolean) {
        binding.run {
            networkStatusOffline.isVisible = !isOnline
            networkStatusOnline.isVisible = isOnline
        }
    }

    private fun setListeners() {
        binding.run {
            addOrEditButton.setOnClickListener {
                viewModel.setSelectedItems(itemListAdapter.getSelectedItems())
                viewModel.navigateToUploadInventoryFragment()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}