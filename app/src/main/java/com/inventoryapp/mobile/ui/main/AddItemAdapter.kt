package com.inventoryapp.mobile.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.inventoryapp.mobile.databinding.ViewHolderAddItemBinding
import com.inventoryapp.mobile.entity.Item
import com.inventoryapp.mobile.entity.isEmptyItem

class AddItemAdapter(
    private val listener: AddItemActionListener
) : RecyclerView.Adapter<AddItemViewHolder>(), AddItemViewHolder.AddItemViewHolderActionListener {

    interface AddItemActionListener {
        fun checkForExistingSkuId(skuId: String, currentPosition: Int)
    }

    val itemList = ArrayList<Item>()

    fun addItems(items: ArrayList<Item>) {
        itemList.clear()
        itemList.addAll(items)
        notifyDataSetChanged()
    }

    override fun updateItem(item: Item, currentPosition: Int) {
        val currentItem = itemList[currentPosition]
        if (currentItem != item) {
            itemList[currentPosition] = item
            notifyItemChanged(currentPosition)
            if (itemCount > 0) {
                if (currentPosition == itemCount - 1 && !isLastRowEmpty) {
                    itemList.add(Item("", "", ""))
                    notifyItemInserted(itemCount)
                }
            }
        }
    }

    override fun checkForExistingSkuId(skuId: String, currentPosition: Int) {
        listener.checkForExistingSkuId(skuId, currentPosition)
    }

    private var isLastRowEmpty =
        if (itemCount > 0)
            itemList[itemCount - 1].isEmptyItem()
        else
            false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddItemViewHolder {
        val binding =
            ViewHolderAddItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddItemViewHolder(binding, this)
    }

    override fun onBindViewHolder(holder: AddItemViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.bindItem(currentItem, position)
        holder.setListeners()
    }

    override fun getItemCount(): Int = itemList.count()

    override fun onCloseRowButtonClicked(position: Int) {
        if (position == itemCount - 1) {
            if (!itemList[position].isEmptyItem()) {
                val list = itemList.filter { item -> item != itemList[position] } as ArrayList<Item>
                list.add(Item("", "", ""))
                addItems(list)
            }
        } else {
            addItems(itemList.filter { item -> item != itemList[position] } as ArrayList<Item>)
        }
    }
}