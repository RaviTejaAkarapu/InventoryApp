package com.inventoryapp.mobile.entity

data class SelectableItem(
    var item: Item,
    var isSelected: Boolean = false
)