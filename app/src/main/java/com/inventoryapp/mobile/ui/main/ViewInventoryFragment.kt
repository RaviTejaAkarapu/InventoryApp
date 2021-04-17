package com.inventoryapp.mobile.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.inventoryapp.mobile.databinding.FragmentViewInventoryBinding
import com.inventoryapp.mobile.entity.SelectableItem
import com.inventoryapp.mobile.networkNotification.NetworkStatus
import com.inventoryapp.mobile.util.Resource
import dagger.hilt.android.AndroidEntryPoint

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
    }

    override fun onResume() {
        super.onResume()
        observeViewModel()
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

//        viewModel.allItemsFromDb.observe(viewLifecycleOwner) {
//            itemListAdapter.setItems(
//                ArrayList(it?.map { item ->
//                    SelectableItem(item)
//                })
//            )
//            handleItemListView(hasItems = !it.isNullOrEmpty())
//        }
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