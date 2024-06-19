package com.capstone.healme.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.capstone.healme.R

import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class PasswordEditText : TextInputEditText {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val parentLayout = parent.parent as TextInputLayout
                s?.let {
                    if (it.length < 8) {
                        parentLayout.error = resources.getString(R.string.password_less_than_eight)
                        parentLayout.isErrorEnabled = true
                    } else {
                        parentLayout.error = null
                        parentLayout.isErrorEnabled = false
                    }
                }
            }
        })
    }
}