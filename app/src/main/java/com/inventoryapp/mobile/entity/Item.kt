package com.inventoryapp.mobile.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Item(
    @PrimaryKey
    val skuId: String,
    val itemName: String?,
    val manufacturerName: String?,
    val quantity: Int? = null
)

fun Item.isEmptyItem(): Boolean = skuId.isEmpty() && quantity == null
