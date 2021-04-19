package com.inventoryapp.mobile.ui.main

import android.view.View
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.inventoryapp.mobile.databinding.ViewHolderAddItemBinding
import com.inventoryapp.mobile.entity.Item
import com.inventoryapp.mobile.util.AfterTextChanged

class AddItemViewHolder(
    private val binding: ViewHolderAddItemBinding,
    private val listener: AddItemViewHolderActionListener
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var currentItem: Item

    private var currentPosition: Int = 0

    private val textWatcher = AfterTextChanged {
        binding.apply {
            if (skuIdEditText.text?.length == 6 && currentItem.skuId != skuIdEditText.text.toString()) {
                listener.checkForExistingSkuId(skuIdEditText.text.toString(), currentPosition)
            }
        }
    }

    private val focusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
        when (view) {
            is EditText -> {
                binding.apply {
                    if (skuIdEditText.text?.length == 6 && !quantityEditText.text.isNullOrEmpty()) {
                        if (skuIdEditText.text.toString() != currentItem.skuId || quantityEditText.text.toString() != currentItem.quantity.toString()) {
                            val updatedItem = Item(
                                skuIdEditText.text.toString(),
                                currentItem.itemName,
                                currentItem.manufacturerName,
                                quantityEditText.text.toString().toInt()
                            )
                            currentItem = updatedItem
                            listener.updateItem(updatedItem, currentPosition)
                        }
                    }
                }
            }
        }
    }

    fun bindItem(item: Item, position: Int) {
        currentItem = item
        currentPosition = position
        binding.apply {
            skuIdEditText.setText(item.skuId)
            skuIdEditText.setSelection(skuIdEditText.text.toString().length)
            quantityEditText.setText(item.quantity?.toString())
            quantityEditText.setSelection(quantityEditText.text.toString().length)
            itemName.text = item.itemName
            manufacturerName.text = item.manufacturerName
            manufacturerName.text = item.manufacturerName
            closeEditRow.setOnClickListener { listener.onCloseRowButtonClicked(position) }
        }
    }

    fun setListeners() {
        binding.apply {
            skuIdEditText.addTextChangedListener(textWatcher)
            quantityEditText.onFocusChangeListener = focusChangeListener
        }
    }

    interface AddItemViewHolderActionListener {
        fun onCloseRowButtonClicked(position: Int)
        fun updateItem(item: Item, currentPosition: Int)
        fun checkForExistingSkuId(skuId: String, currentPosition: Int)
    }
}
