package com.inventoryapp.mobile.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.inventoryapp.mobile.R
import com.inventoryapp.mobile.databinding.FragmentViewInventoryBinding
import com.inventoryapp.mobile.entity.SelectableItem
import com.inventoryapp.mobile.util.Resource

class SearchInventoryFragment : Fragment(), ItemListAdapter.ItemActionListener {

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
            itemListAdapter = ItemListAdapter(this@SearchInventoryFragment)
            adapter = itemListAdapter
        }
        binding.apply {
            addOrEditButton.text = getString(R.string.select_button_text)
            fragmentTitle.text = getString(R.string.search_inventory_fragment_name)
        }
    }

    private fun setListeners() {
        binding.apply {
            addOrEditButton.setOnClickListener {
                viewModel.navigateToUploadInventoryFragment()
            }
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
                            SelectableItem(item)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun setEditButtonClickable() {
//        NA
    }
}