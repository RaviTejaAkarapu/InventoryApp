package com.inventoryapp.mobile.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.inventoryapp.mobile.databinding.FragmentViewInventoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewInventoryFragment : Fragment() {

    private val viewModel by viewModels<InventoryViewModel>()

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
    }

    private fun setListeners() {
        binding.run {

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}