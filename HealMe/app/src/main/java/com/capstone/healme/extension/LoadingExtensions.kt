package com.capstone.healme.extension

import android.view.View
import android.widget.ProgressBar

fun setLoading(progressBar: ProgressBar, isLoading: Boolean) {
    if (isLoading) progressBar.visible() else progressBar.gone()
}

fun setCustomLoading(progressBar: ProgressBar, view: View, isLoading: Boolean) {
    if (isLoading) {
        progressBar.visible()
        view.gone()
    } else {
        progressBar.gone()
        view.visible()
    }
}