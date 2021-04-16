package com.inventoryapp.mobile.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.versionedparcelable.ParcelField

@Entity
data class Item(
    @PrimaryKey
    val skuId: String,
    val itemName: String?,
    val manufacturerName: String?,
    val quantity: Int? = null
)

fun Item.isEmptyItem(): Boolean = skuId.isEmpty() && quantity == null
//data class ItemDetails(
//    val quantity: Int?,
//    val measuringUnits: Units
//)
//
//enum class Units { ML, LTR, CM, MTR, MG, GM, KG }