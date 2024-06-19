package com.capstone.healme.extension

import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.Fragment

fun Fragment.setLoading(progressBar: ProgressBar, isLoading: Boolean) {
    if (isLoading) progressBar.visible() else progressBar.gone()
}

fun Fragment.setCustomLoading(progressBar: ProgressBar, view: View, isLoading: Boolean) {
    if (isLoading) {
        progressBar.visible()
        view.gone()
    } else {
        progressBar.gone()
        view.visible()
    }
}