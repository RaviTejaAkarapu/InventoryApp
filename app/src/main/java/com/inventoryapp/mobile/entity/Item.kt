package com.inventoryapp.mobile.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Item(
    @PrimaryKey
    val skuId: String,
    val manufacturerName: String?,
    val quantity: Int?
)

//data class ItemDetails(
//    val quantity: Int?,
//    val measuringUnits: Units
//)
//
//enum class Units { ML, LTR, CM, MTR, MG, GM, KG }