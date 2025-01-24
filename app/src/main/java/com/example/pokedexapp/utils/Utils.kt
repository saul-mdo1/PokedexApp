package com.example.pokedexapp.utils

import android.content.Context
import com.example.pokedexapp.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun showAlertError(context: Context, message: String, onPositiveButton: () -> Unit) {
    MaterialAlertDialogBuilder(context)
        .setTitle(context.resources.getString(R.string.txt_error))
        .setMessage(message)
        .setCancelable(false)
        .setPositiveButton(context.resources.getString(R.string.txt_retry)) { dialog, _ ->
            onPositiveButton()
            dialog.dismiss()
        }
        .setNegativeButton(context.resources.getString(R.string.txt_accept)) { dialog, _ ->
            dialog.dismiss()
        }
        .show()
}