package com.capstone.healme.extension

import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.showToast(message: String, isShort: Boolean) {
    if (isShort) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    } else {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }
}