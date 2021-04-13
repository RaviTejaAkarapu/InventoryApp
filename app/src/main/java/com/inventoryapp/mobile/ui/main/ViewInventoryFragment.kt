package com.inventoryapp.mobile.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.inventoryapp.mobile.databinding.FragmentViewInventoryBinding
import com.inventoryapp.mobile.networkNotification.NetworkStatus
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewInventoryFragment : Fragment() {

    private val viewModel by activityViewModels<InventoryViewModel>()

    private var _binding: FragmentViewInventoryBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewInventoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.networkStatusLiveData.observe(viewLifecycleOwner) { networkStatus ->
            when (networkStatus) {
                NetworkStatus.Offline -> setNetworkStatusView(isOnline = false)
                NetworkStatus.Online -> setNetworkStatusView(isOnline = true)
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
        binding.run {
            addOrEditButton.setOnClickListener { viewModel.navigateToUploadInventoryFragment() }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}