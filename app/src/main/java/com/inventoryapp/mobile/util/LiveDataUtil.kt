package com.inventoryapp.mobile.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> LiveData<T>.observeForChange(owner: LifecycleOwner, observer: Observer<T>) {
    var previousValue = value
    observe(owner) { currentValue ->
        if (currentValue!=previousValue) {
            previousValue = currentValue
            observer.onChanged(currentValue)
        }
    }
}