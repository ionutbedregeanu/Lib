package com.lib.ui.utils

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

fun AppCompatActivity.displaySnackBar(rootView: View, backgroundColorId: Int, messageId: Int) {
    Snackbar.make(rootView, messageId, Snackbar.LENGTH_LONG)
        .setBackgroundTint(resources.getColor(backgroundColorId, null))
        .show()
}
