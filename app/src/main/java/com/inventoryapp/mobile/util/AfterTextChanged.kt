package com.inventoryapp.mobile.util

import android.text.Editable
import android.text.TextWatcher

class AfterTextChanged(private val listener: (s: Editable) -> Unit): TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

    override fun afterTextChanged(s: Editable) = listener.invoke(s)

}