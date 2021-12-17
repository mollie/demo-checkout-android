package com.mollie.checkout.feature

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.os.bundleOf
import com.mollie.checkout.R
import com.mollie.checkout.Settings
import com.mollie.checkout.data.model.Payment
import com.mollie.checkout.data.model.PaymentFlow
import com.mollie.checkout.feature.inappbrowser.InAppBrowserActivity
import com.mollie.checkout.feature.components.MollieAlert
import com.mollie.checkout.feature.payments.PaymentsActivity
import com.mollie.checkout.feature.payments.create.CreatePaymentActivity
import com.mollie.checkout.feature.payments.selectcheckout.SelectCheckoutActivity
import kotlin.reflect.KClass

object Navigation {

    fun openPayments(context: Context?, completedPaymentId: Int? = null) {
        startActivity(context, PaymentsActivity::class, bundleOf(
            Pair(PaymentsActivity.EXTRA_COMPLETED_PAYMENT_ID, completedPaymentId)
        ))
    }

    fun openCreatePayment(context: Context?, payment: Payment? = null) {
        val bundle = bundleOf(Pair(CreatePaymentActivity.EXTRA_PAYMENT, payment))
        startActivity(context, CreatePaymentActivity::class, bundle)
    }

    fun openSelectCheckout(context: Context?, payment: Payment) {
        context ?: return
        val bundle = bundleOf(Pair(SelectCheckoutActivity.EXTRA_PAYMENT, payment))
        startActivity(context, SelectCheckoutActivity::class, bundle)
    }

    fun openPaymentFlow(context: Context?, payment: Payment?, finished: (() -> Unit)? = null) {
        context ?: return
        payment?.url ?: return
        when (Settings.Navigation.PAYMENT_FLOW) {
            PaymentFlow.CHOOSE -> {
                MollieAlert.show(
                    context,
                    title = context.getString(R.string.payments_checkout_select_flow_title),
                    description = context.getString(R.string.payments_checkout_select_flow_description),
                    primary = context.getString(R.string.payments_checkout_select_flow_external_browser),
                    primaryAction = {
                        openUrl(context, payment.url)
                        finished?.invoke()
                    },
                    secondary = context.getString(R.string.payments_checkout_select_flow_in_app_browser),
                    secondaryAction = {
                        openCheckout(context, payment)
                        finished?.invoke()
                    },
                    cancelable = false,
                )
            }
            PaymentFlow.EXTERNAL_BROWSER -> {
                openUrl(context, payment.url)
                finished?.invoke()
            }
            PaymentFlow.IN_APP_BROWSER -> {
                openCheckout(context, payment)
                finished?.invoke()
            }
        }
    }

    private fun openCheckout(context: Context?, payment: Payment?) {
        val bundle = bundleOf(Pair(InAppBrowserActivity.EXTRA_PAYMENT, payment))
        startActivity(context, InAppBrowserActivity::class, bundle)
    }

    private fun openUrl(context: Context?, url: String) {
        context ?: return

        val builder = CustomTabsIntent.Builder()
            .setShowTitle(true)

        val customTabsIntent = builder.build()
        customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        customTabsIntent.launchUrl(context, Uri.parse(url))
    }

    private fun startActivity(context: Context?, activityClass: KClass<*>, bundle: Bundle? = null) {
        context ?: return
        val intent = Intent(context, activityClass.java)
        bundle?.let {
            intent.putExtras(bundle)
        }
        context.startActivity(intent)
    }
}