package com.hypnex.friengo.utils

import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

object DialogUtils {

    class DialogWithEditText(private val yesOption: (editText: EditText) -> Unit) :
        DialogFragment() {

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val input = EditText(requireContext())

            return AlertDialog.Builder(requireContext())
                .setTitle("Введите E-mail")
                .setView(input)
                .setPositiveButton(android.R.string.ok) { dialog, which ->
                    yesOption(input)
                }
                .setNegativeButton(android.R.string.cancel, null)
                .create()
        }
    }

}