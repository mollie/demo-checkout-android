<img align="right" src="images/FlowBasic.gif" alt="Basic flow" width="24%" />

# Flow 1: Basic implementation (recommended)

The basic implementation consists of creating and executing the payment and handling the result when the payment is completed.

The payment url is being opened in an external browser handle the payment process.

## Step 1: Create payment

The bare minimum needed to create a payment is a `description` and the `amount`. Usually these values are determined based on what is being bought.

Create a payment by executing the backend call which creates the payment with these values.

> **Note:** It is required define the `amount` of the payment in a safe environment (the backend). Mollie Checkout uses user input here only for demonstration purposes.

## Step 2: Executing payment

After successfully creating the payment, the resulting payment object contains an `url`. This is the checkout url of the payment which should be opened for the user to execute the payment.

Open the `url` by launching an intent:

```kotlin
val intent = Intent(Intent.ACTION_VIEW, Uri.parse(paymentUrl))
startActivity(intent)
```

> **Note:** Optionally, the url can be launched in a custom way. For example: Mollie Checkout uses _Chrome Custom Tabs_ which enables some styling in case the user has Chrome set as default browser, see the `openUrl()` method in [Navigation.kt](app/src/main/java/com/mollie/checkout/feature/Navigation.kt).

## Step 3: Handling payment result

Generally the payment result comes back via the deeplink that is configured by the backend when creating the payment. 

Deeplinks are url's that have a custom scheme which can take users directly to a specific part of your app. To learn more about deeplinks, check out the [documentation](https://developer.android.com/training/app-links/deep-linking).

For the demo app the deeplink values are the following:

```xml
<string name="deeplink_host">payments</string>
<string name="deeplink_scheme_app">mollie-checkout</string>
```

Determine the Activity that will handle the deeplink. Add the deeplink configuration to the Activity in `AndroidManifest.xml`:

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

> **Note:** In the demo app we use `android:launchMode="singleTop"` to make sure we always have only one Activity of this instance that always shows the latest state. Check out the [documentation](https://developer.android.com/guide/topics/manifest/activity-element) for more information about the launch mode.

In the Activity, add a method to check whether the new intent was the deeplink:

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

## Step 4: Refresh payment status

It is recommended to refresh the payment(s) when the user returns to the app, because there are a few exceptional cases where the user can successfully complete the payment while preventing the return via the deeplink.

In the `onResume()` of the Activity, refresh the payment(s):

```kotlin
override fun onResume() {
    super.onResume()
    vm.refresh() // Refreshing the payments list when opening this screen to ensure latest state of the payments
}
```

# Additions

After implementing the base flow, the following additions are available:

- [Flow 2: Advanced implementation](FLOW_ADVANCED.md)
- [Optional: Implement payment methods](IMPLEMENT_PAYMENT_METHODS.md)

# Resources

Related samples in Mollie Checkout:

- [PaymentsActivity.kt](app/src/main/java/com/mollie/checkout/feature/payments/PaymentsActivity.kt): refreshing payments & handling the return of a completed payment.
- [CreatePaymentActivity.kt](app/src/main/java/com/mollie/checkout/feature/payments/create/CreatePaymentActivity.kt): creation of the payment.
