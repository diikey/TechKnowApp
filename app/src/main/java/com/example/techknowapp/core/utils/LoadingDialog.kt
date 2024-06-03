package com.example.techknowapp.core.utils

import android.app.AlertDialog
import android.content.Context
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.view.ViewGroup.LayoutParams

class LoadingDialog(private val context: Context) {
    private var dialog: AlertDialog? = null

    fun show(message: String = "Loading...") {
        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(50, 50, 50, 50)
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            gravity = Gravity.CENTER
        }

        val progressBar = ProgressBar(context).apply {
            isIndeterminate = true
        }

        val textView = TextView(context).apply {
            text = message
            setPadding(20, 0, 0, 0)
        }

        layout.addView(progressBar)
        layout.addView(textView)

        dialog = AlertDialog.Builder(context)
            .setView(layout)
            .setCancelable(false)
            .create()

        dialog?.show()
    }

    fun dismiss() {
        dialog?.dismiss()
    }
}