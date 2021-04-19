package com.inventoryapp.mobile.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.inventoryapp.mobile.R
import com.inventoryapp.mobile.databinding.FragmentViewInventoryBinding
import com.inventoryapp.mobile.entity.Item
import com.inventoryapp.mobile.entity.SelectableItem
import com.inventoryapp.mobile.util.AfterTextChanged
import com.inventoryapp.mobile.util.Resource
import com.inventoryapp.mobile.util.observeForChange
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class InventoryFragment : Fragment(), ItemListAdapter.ItemActionListener {
    private var _binding: FragmentViewInventoryBinding? = null
    private val binding
        get() = _binding!!
    private val viewModel by activityViewModels<InventoryViewModel>()

    private lateinit var itemListAdapter: ItemListAdapter
    private lateinit var fragmentView: InventoryViewAction

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewInventoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: InventoryFragmentArgs by navArgs()
        fragmentView = args.InventoryViewAction

        setView()
        setListeners()
        observeViewModel()
        viewModel.getAllItemsFromDb()
    }

    private fun setView() {
        binding.itemRecyclerView.apply {
            itemListAdapter = ItemListAdapter(this@InventoryFragment)
            adapter = itemListAdapter
        }

        binding.apply {
            fragmentTitle.text =
                if (fragmentView == InventoryViewAction.VIEW)
                    getString(R.string.view_inventory_fragment_name)
                else
                    getString(R.string.search_inventory_fragment_name)

            addOrEditButton.text =
                if (fragmentView == InventoryViewAction.VIEW)
                    getString(R.string.edit_button_text)
                else
                    getString(R.string.select_button_text)

            itemSearchBox.addTextChangedListener(textWatcher)

            manufacturerDropdownBox.apply {
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
                                viewModel.currentItemList.let {
                                    itemListAdapter.setItems(it.map { item ->
                                        SelectableItem(item, isSelected = false)
                                    } as ArrayList<SelectableItem>)
                                }
                            } else {
                                val itemsFromManufacturer =
                                    viewModel.currentItemList.map { SelectableItem(it, false) }
                                        .filter { selectableItem ->
                                            selectableItem.item.manufacturerName.equals(
                                                manufacturerList[position]
                                            )
                                        }
                                itemListAdapter.setItems(
                                    itemsFromManufacturer as ArrayList<SelectableItem>
                                )
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                        }
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

    private fun observeViewModel() {
        viewModel.networkStatusLiveData.observe(viewLifecycleOwner) { networkStatus ->
            networkStatus?.let {
                setNetworkStatusView(isOnline = it.body() == true)
            } ?: setNetworkStatusView(isOnline = false)
        }

        if (fragmentView == InventoryViewAction.SEARCH)
            viewModel.allItemsLiveData.observe(viewLifecycleOwner) {
                when (it.status) {
                    Resource.Status.Success -> {
                        viewModel.currentItemList = it.data ?: mutableListOf()
                        binding.progressBar.isVisible = false
                        itemListAdapter.setItems(
                            ArrayList(it.data?.map { item ->
                                SelectableItem(item, isSelected = false)
                            })
                        )
                        handleItemListView(hasItems = !it.data.isNullOrEmpty())
                    }
                    Resource.Status.Error -> {
                        binding.progressBar.isVisible = false
                        Snackbar.make(
                            requireContext(),
                            binding.root,
                            it.message.toString(),
                            Snackbar.LENGTH_LONG
                        )
                    }
                    Resource.Status.Loading -> {
                        binding.emptyInventoryView.isVisible = false
                        binding.progressBar.isVisible = true
                    }
                }
            }
        else
            viewModel.allItemsFromDb.observeForChange(viewLifecycleOwner) { itemList ->
                viewModel.currentItemList = itemList
                binding.progressBar.isVisible = true
                binding.emptyInventoryView.isVisible = itemList.isNullOrEmpty()
                itemListAdapter.setItems(
                    ArrayList(itemList?.map { item ->
                        SelectableItem(item, isSelected = false)
                    })
                )
                binding.progressBar.isVisible = false
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

    enum class InventoryViewAction {
        VIEW,
        SEARCH
    }
}