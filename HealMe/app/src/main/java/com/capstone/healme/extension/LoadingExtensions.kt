package com.capstone.healme.extension

import android.widget.ProgressBar
import androidx.fragment.app.Fragment

fun Fragment.setLoading(progressBar: ProgressBar, isLoading: Boolean) {
    if (isLoading) progressBar.visible() else progressBar.gone()
}