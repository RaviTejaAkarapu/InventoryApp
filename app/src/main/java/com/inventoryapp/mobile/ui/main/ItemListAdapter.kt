package com.inventoryapp.mobile.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.inventoryapp.mobile.databinding.ViewHolderListItemBinding
import com.inventoryapp.mobile.entity.Item

class ItemListAdapter(
    private val listener: ItemActionListener
): RecyclerView.Adapter<ListItemViewHolder>() {

    interface ItemActionListener {
        fun onClickedItem(skuId: String)
    }

    private val itemList = ArrayList<Item>()

    fun setItems(items: ArrayList<Item>) {
        this.itemList.clear()
        this.itemList.addAll(items)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val binding =
            ViewHolderListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListItemViewHolder(binding)
    }

    override fun getItemCount() = itemList.size
    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.itemView.setOnClickListener {
//            holder.itemView.
        }
        holder.bindItem(currentItem)
    }

}