package com.capstone.healme.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.capstone.healme.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class EmailEditText : TextInputEditText {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$".toRegex()
                val parentLayout = parent.parent as TextInputLayout
                s?.let {
                    if (!it.matches(emailRegex)) {
                        parentLayout.error = resources.getString(R.string.email_invalid)
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