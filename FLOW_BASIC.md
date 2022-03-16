# Implementing the basic flow

<img align="right" src="images/FlowBasic.gif" alt="Basic flow" width="24%" />

The basic implementation is the same as executing a payment in the web, using a browser on the customer’s device.

## Behaviour

When customers create a payment in your app, they receive a payment link. The link launches an external browser on the device where the customer selects their payment method and completes the payment. Depending on the selected payment method, the device might launch a native app from the browser to complete the payment.

After the payment is processed, a deep link takes the customer back to the app to see the payment result. The app refreshes the payments to show their latest statuses.

> ✅ **Tip**: We recommend this flow if you want to add Mollie payments to your app with minimum effort.

## Implementation

The basic flow consists of the following steps:

1.  [Create a payment](#step-1-create-a-payment)
2.  [Execute the payment](#step-2-execute-the-payment)
3.  [Handle the completed payment](#step-3-handle-the-completed-payment)
4.  [Refresh the payment status](#step-4-refresh-the-payment-status)

### Step 1: Create a payment

To create a payment, execute the backend call that creates a payment specifying a `description` and an `amount`.

This returns a payment object which contains the URL needed to execute the payment.

> :warning: **Note**: You must define the payment `amount` in a secure environment (the backend). Mollie Checkout for Android only uses user input here for demonstration purposes.

### Step 2: Execute the payment

To navigate to the checkout, launch an intent to open the URL from the create payment response.

```kotlin
val intent = Intent(Intent.ACTION_VIEW, Uri.parse(paymentUrl))
startActivity(intent)
```

> ✅  **Tip**: You can customise how the `paymentUrl` is opened. Mollie Checkout for Android uses Chrome Custom Tabs, which allows for some styling for customers that use Chrome as their default browser. See the `openUrl()` method in [Navigation.kt](app/src/main/java/com/mollie/checkout/feature/Navigation.kt).

### Step 3: Handle the completed payment

In general, the payment result returns through a [deep link](https://developer.android.com/training/app-links/deep-linking) that is configured by the backend when the payment was created.

There are two deep link values in the app:

```xml
<string name="deeplink_host">payments</string>
<string name="deeplink_scheme_app">mollie-checkout</string>
```

To navigate customers back to your app after they complete their payment, follow the steps below.

1.  Determine which Activity should handle the deep link.
2.  Add the configuration to the activity in [AndroidManifest.xml](app/src/main/AndroidManifest.xml):

```xml
<activity
    android:name=".feature.payments.PaymentsActivity"
    android:launchMode="singleTop"
    android:exported="true">

    <intent-filter>
        <data
            android:host="@string/deeplink_host"
            android:scheme="@string/deeplink_scheme_app" />
    </intent-filter>
</activity>
```

> ℹ️ **Info**: Mollie Checkout for Android uses `android:launchMode="singleTop"` as its [launch mode](https://developer.android.com/guide/topics/manifest/activity-element) to ensure only one Activity in the instance displays the latest payment statuses.

Afterwards, add a method to the Activity to confirm that the new intent uses the deep link.

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState)
  // ...
  checkCompletedPayment(intent)
}

override fun onNewIntent(intent: Intent?) {
  super.onNewIntent(intent)
  checkCompletedPayment(intent)
}

private fun checkCompletedPayment(intent: Intent?) {
    val isFromDeeplink = intent.data?.scheme == getString(R.string.deeplink_scheme_app)
    if (isFromDeeplink) {
      // Payment data when called from deeplink
      val paymentId = intent.data?.path?.substring(1) // `path` starts with a `/`, removing that
    }
}
```

### Step 4: Refresh the payment status

In some cases, customers can successfully complete their payment without returning to the app through the deep link. You should therefore refresh payments when customers return to the app, so that they see the latest statuses.

Refresh the payments in the `onResume()` method in the Activity.

```kotlin
override fun onResume() {
    super.onResume()
    vm.refresh() // Refreshing the payments list when opening this screen to ensure latest state of the payments
}
```

## Next steps

After implementing the basic flow, your app can handle Mollie payments using an external browser. You can further customise this flow through two additional implementations:
-   [Including payment method selection](IMPLEMENT_PAYMENT_METHODS.md) in your app.  
-   [Implement the advanced flow](FLOW_ADVANCED.md) to handle payments in a customisable in-app browser.

## Resources

Refer to the following source files for relevant samples:
-   [PaymentsActivity.kt](app/src/main/java/com/mollie/checkout/feature/payments/PaymentsActivity.kt): refresh payments and handle a completed payment.
-   [CreatePaymentActivity.kt](app/src/main/java/com/mollie/checkout/feature/payments/create/CreatePaymentActivity.kt): create a payment.
