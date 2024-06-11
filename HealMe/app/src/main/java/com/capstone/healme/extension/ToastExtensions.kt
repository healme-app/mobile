package com.capstone.healme.extension

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment

fun Context.showToast(message: String, isShort: Boolean) {
    if (isShort) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    } else {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}

// Extension function for Fragment to show a toast message
fun Fragment.showToast(message: String, isShort: Boolean) {
    if (isShort) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    } else {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }
}