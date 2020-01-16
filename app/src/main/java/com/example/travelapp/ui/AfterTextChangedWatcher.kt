package com.example.travelapp.ui

import android.text.Editable
import android.text.TextWatcher

interface AfterTextChangedWatcher : TextWatcher {

    override fun afterTextChanged(s: Editable?)

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
}