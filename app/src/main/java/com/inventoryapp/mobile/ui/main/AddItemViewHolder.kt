package com.inventoryapp.mobile.ui.main

import androidx.recyclerview.widget.RecyclerView
import com.inventoryapp.mobile.databinding.ViewHolderAddItemBinding
import com.inventoryapp.mobile.entity.Item
import com.inventoryapp.mobile.util.AfterTextChanged

class AddItemViewHolder(
    private val binding: ViewHolderAddItemBinding,
    private val listener: AddItemViewHolderActionListener
) : RecyclerView.ViewHolder(binding.root) {

    private var addedEmptyRow = false

    private lateinit var currentItem: Item

    private var currentPosition: Int = 0

    interface AddItemViewHolderActionListener {
        fun onCloseRowButtonClicked(position: Int)
        fun updateItem(item: Item, currentPosition: Int, addedEmptyRow: Boolean): Boolean
        fun updateItem(item: Item, currentPosition: Int)
//        fun checkForExistingSkuId(skuId: ): Item?
    }

    fun bindItem(item: Item, position: Int) {
        currentItem = item
        currentPosition = position
        binding.apply {
            skuIdEditText.setText(item.skuId)
            skuIdEditText.setSelection(skuIdEditText.text.toString().length)
            quantityEditText.setText(item.quantity?.toString())
            quantityEditText.setSelection(quantityEditText.text.toString().length)
            closeEditRow.setOnClickListener { listener.onCloseRowButtonClicked(position) }
        }
    }

    fun setLastRowListeners() {
        binding.apply {
            skuIdEditText.addTextChangedListener(lastRowTextWatcher)
            quantityEditText.addTextChangedListener(lastRowTextWatcher)
        }
    }

    fun setListeners() {
            binding.apply {
                skuIdEditText.addTextChangedListener(textWatcher)
                quantityEditText.addTextChangedListener(textWatcher)
            }
    }

    private val textWatcher = AfterTextChanged {
        binding.apply {
            if (!skuIdEditText.text.isNullOrEmpty() && !quantityEditText.text.isNullOrEmpty()) {
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

    private val lastRowTextWatcher = AfterTextChanged {
//        if(itemView.id == binding.skuIdEditText.id)
//            checkForExistingSkuId()
        validateEditTexts()
    }

    private fun validateEditTexts() {
        binding.apply {
            if (!skuIdEditText.text.isNullOrEmpty() && !quantityEditText.text.isNullOrEmpty()) {
                if (skuIdEditText.text.toString() != currentItem.skuId || quantityEditText.text.toString() != currentItem.quantity.toString()) {
                    val updatedItem = Item(
                        skuIdEditText.text.toString(),
                        currentItem.itemName,
                        currentItem.manufacturerName,
                        quantityEditText.text.toString().toInt()
                    )
                    currentItem = updatedItem
                    addedEmptyRow = listener.updateItem(updatedItem, currentPosition, addedEmptyRow)
                }
            }



                if (!skuIdEditText.text.isNullOrEmpty() && !quantityEditText.text.isNullOrEmpty()) {
                if (skuIdEditText.text.toString() != currentItem.skuId || quantityEditText.text.toString() != currentItem.quantity.toString()) {
                    val updatedItem = Item(
                        skuIdEditText.text.toString(),
                        currentItem.itemName,
                        currentItem.manufacturerName,
                        quantityEditText.text.toString().toInt()
                    )
                    currentItem = updatedItem
                    addedEmptyRow = listener.updateItem(updatedItem, currentPosition, addedEmptyRow)
                }
            }
        }
    }
}