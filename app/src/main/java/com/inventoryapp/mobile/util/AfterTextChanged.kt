package com.inventoryapp.mobile.util

import android.os.SystemClock
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import java.util.*

private const val interval = 1000L

class AfterTextChanged(
    private val view: View? = null,
    private val listener: (s: Editable) -> Unit
) : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

    override fun afterTextChanged(s: Editable) {
        view?.let { view ->
            val lastChanged: MutableMap<View, Long> = WeakHashMap()
            val previousChangeTime = lastChanged[view]
            val currentTime = SystemClock.uptimeMillis()
            lastChanged[view] = currentTime
            if (previousChangeTime == null || currentTime - previousChangeTime.toLong() > interval)
                listener.invoke(s)
        } ?: listener.invoke(s)
    }
}