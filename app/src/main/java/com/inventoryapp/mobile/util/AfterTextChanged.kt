package com.inventoryapp.mobile.util

import android.text.Editable
import android.text.TextWatcher
import java.util.*

private const val interval = 1000L

class AfterTextChanged(private val listener: (s: Editable) -> Unit) : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

    override fun afterTextChanged(s: Editable) {
        val lastChanged: MutableMap<Editable, Long> = WeakHashMap()
//
//        val previousChangeTime = lastChanged[s]
//        val currentTime = SystemClock.uptimeMillis()
//
//        lastChanged[s] = currentTime
//        if (previousChangeTime == null || currentTime - previousChangeTime.toLong() > interval)
        listener.invoke(s)
    }
}