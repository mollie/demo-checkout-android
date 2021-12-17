package com.mollie.checkout.feature.components

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.mollie.checkout.databinding.AlertCustomBinding
import com.mollie.checkout.util.setGone

object MollieAlert {

    fun show(
        context: Context,
        title: String,
        description: String,
        primary: String? = null,
        primaryAction: (() -> Unit)? = null,
        secondary: String? = null,
        secondaryAction: (() -> Unit)? = null,
        cancelable: Boolean = true,
    ) {
        val builder = AlertDialog.Builder(context)
            .setCancelable(cancelable)

        val alertBinding = AlertCustomBinding.inflate(LayoutInflater.from(context))
        alertBinding.title.text = title
        alertBinding.description.text = description
        alertBinding.primaryButton.text = primary
        alertBinding.primaryButton.setGone(primary == null)
        alertBinding.secondaryButton.text = secondary
        alertBinding.secondaryButton.setGone(secondary == null)

        builder.setView(alertBinding.root)

        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        alertBinding.primaryButton.setOnClickListener {
            primaryAction?.invoke()
            dialog.dismiss()
        }
        alertBinding.secondaryButton.setOnClickListener {
            secondaryAction?.invoke()
            dialog.dismiss()
        }

        dialog.show()
    }
}