package com.inventoryapp.mobile.ui.main

import androidx.recyclerview.widget.RecyclerView
import com.inventoryapp.mobile.R
import com.inventoryapp.mobile.databinding.ViewHolderListItemBinding
import com.inventoryapp.mobile.entity.Item

class ListItemViewHolder(private val binding: ViewHolderListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bindItem(item: Item) {
        binding.apply {
            itemName.text = item.itemName
            manufacturerName.text = item.manufacturerName
            itemQuantity.text = item.quantity?.toString() ?: "--"
        }
    }

    fun setItemViewBgColor(isSelected: Boolean) {
        binding.root.apply {
            setBackgroundResource(
                when (isSelected) {
                    true -> R.drawable.list_item_selected_border
                    else -> R.drawable.list_item_border
                }
            )
        }
    }
}