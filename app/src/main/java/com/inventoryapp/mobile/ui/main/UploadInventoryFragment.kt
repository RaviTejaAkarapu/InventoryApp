package com.inventoryapp.mobile.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.inventoryapp.mobile.databinding.FragmentUploadInventoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UploadInventoryFragment : Fragment() {

    private val viewModel by activityViewModels<InventoryViewModel>()

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}