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

    private val itemList = ArrayList<Item>()

    fun addItems(items: ArrayList<Item>) {
        itemList.clear()
        itemList.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddItemViewHolder {
        val binding =
            ViewHolderAddItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddItemViewHolder(binding, this)
    }

    override fun onBindViewHolder(holder: AddItemViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.bindItem(currentItem)

    }

    override fun getItemCount(): Int = itemList.count()

    override fun onCloseRowButtonClicked(skuId: String) {
        addItems(itemList.filter { item -> item.skuId != skuId } as ArrayList<Item>)
    }

    override fun addEmptyRow(currentItem: Item) {
        itemList.add(Item("", "", "", null))
        notifyDataSetChanged()
    }
}