package com.inventoryapp.mobile.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.inventoryapp.mobile.databinding.ViewHolderAddItemBinding
import com.inventoryapp.mobile.entity.Item

class AddItemAdapter(
    private val addItemActionListener: AddItemActionListener
) : RecyclerView.Adapter<AddItemViewHolder>(), AddItemViewHolder.AddItemViewHolderActionListener {

    interface AddItemActionListener {

    }

    val itemList = ArrayList<Item>()

    fun addItems(items: ArrayList<Item>) {
        itemList.clear()
        itemList.addAll(items)
        notifyDataSetChanged()
    }

    override fun updateItem(item: Item, currentPosition: Int, addedEmptyRow: Boolean): Boolean {
        itemList[currentPosition] = item
        if (currentPosition == itemCount - 1 && !addedEmptyRow) {
            itemList.add(Item("", "", ""))
            notifyItemInserted(itemCount)
            return true
        }
        return addedEmptyRow
    }

    override fun updateItem(item: Item, currentPosition: Int) {
        itemList[currentPosition] = item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddItemViewHolder {
        val binding =
            ViewHolderAddItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddItemViewHolder(binding, this)
    }

    override fun onBindViewHolder(holder: AddItemViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.bindItem(currentItem, position)
        if (position == itemCount - 1)
            holder.setLastRowListeners()
        else
            holder.setListeners()
    }

    override fun getItemCount(): Int = itemList.count()

    override fun onCloseRowButtonClicked(position: Int) {
        if (itemCount > 1)
            addItems(itemList.filter { item -> item != itemList[position] } as ArrayList<Item>)
        else
            addItems(arrayListOf(Item("", "", "")))
    }
}