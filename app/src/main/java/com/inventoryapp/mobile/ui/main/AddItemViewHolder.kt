package com.inventoryapp.mobile.ui.main

import android.text.Editable
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.internal.TextWatcherAdapter
import com.inventoryapp.mobile.databinding.ViewHolderAddItemBinding
import com.inventoryapp.mobile.entity.Item

class AddItemViewHolder(
    private val binding: ViewHolderAddItemBinding,
    private val listener: AddItemViewHolderActionListener
) : RecyclerView.ViewHolder(binding.root) {
    interface AddItemViewHolderActionListener {
        fun onCloseRowButtonClicked(skuId: String)
        fun addEmptyRow()
    }

    fun bindItem(item: Item) {
        binding.apply {
            skuIdEditText.setText(item.skuId)
            quantityEditText.setText(item.manufacturerName)
            closeEditRow.setOnClickListener { listener.onCloseRowButtonClicked(item.skuId) }
            skuIdEditText.addTextChangedListener(object : TextWatcherAdapter() {
                override fun afterTextChanged(s: Editable) {
                    validateEditTexts()
                }
            })
            quantityEditText.addTextChangedListener(object : TextWatcherAdapter() {
                override fun afterTextChanged(s: Editable) {
                    validateEditTexts()
                }
            })
        }
    }

    private fun validateEditTexts() {
        binding.apply {
            if (!skuIdEditText.text.isNullOrEmpty() && !quantityEditText.text.isNullOrEmpty()) {
                listener.addEmptyRow()
            }
        }
    }
}