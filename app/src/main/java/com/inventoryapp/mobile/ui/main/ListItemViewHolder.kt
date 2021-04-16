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

    fun setItemViewBgColor(isSelected: Boolean?) {
        binding.root.apply {
            setBackgroundColor(
                when (isSelected) {
                    true -> context.getColor(R.color.online_background)
                    else -> context.getColor(R.color.white)
                }
            )
        }
    }
}