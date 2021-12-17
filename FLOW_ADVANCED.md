<img align="right" src="images/FlowAdvanced.gif" alt="Advanced flow" width="24%" />

# Flow 2: Advanced implementation

The advanced implementation makes use of a WebView to handle the payments in-app.

> **Note:** Make sure to first implement the basic flow ([Flow 1: Basic implementation (recommended)](FLOW_BASIC.md)) before implementing this advanced flow.

## Step 1: Creating the in-app browser Activity

Instead of launching the payment url in the basic flow, create an Activity that contains a WebView to load the url in.

## Step 2: Set WebView settings

Configure the settings on the WebView:

```kotlin
binding.webView.settings.javaScriptEnabled = true // Needed for the Mollie and issuer sites to load
binding.webView.settings.domStorageEnabled = true // Needed for some issuers to load that makes use HTML 5 specifications, such as Rabobank when generating QR codes
```

## Step 3: Configure WebViewClient

Configure the WebViewClient on the WebView to handle callbacks and urls:

```kotlin
binding.webView.webViewClient = object : WebViewClient() {

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        vm.onWebViewStartedLoading()
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        vm.onWebViewFinishedLoading()
    }

    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        super.onReceivedError(view, request, error)
        Snackbar.make(binding.root, error.toString(), Snackbar.LENGTH_LONG).show()
    }

    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        val uri = Uri.parse(url)
        return handleUri(uri)
    }

    override fun shouldOverrideUrlLoading(
        view: WebView?,
        request: WebResourceRequest?
    ): Boolean {
        val uri = request?.url
        return handleUri(uri)
    }

    private fun handleUri(uri: Uri?): Boolean {
        uri?.scheme ?: return false
        val isMollieUrl =
            getString(R.string.in_app_browser_host_mollie) == uri.host?.replace("www.", "")
        when {
            isMollieUrl -> return false
            uri.scheme == getString(R.string.deeplink_scheme_app) -> {
                // It's our deeplink
                finish()
                val intent = Intent(this@InAppBrowserActivity, PaymentsActivity::class.java)
                intent.data = uri // Trigger as if coming from deeplink
                startActivity(intent)
                return true
            }
            uri.scheme == getString(R.string.deeplink_scheme_https) -> {
                // Normal URL inside WebView, loading it inside the WebView (triggers universal links directly if possible)
                return false
            }
            else -> {
                // Handling deeplinks
                return try {
                    // Try to launch externally
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = uri
                    startActivity(intent)
                    true
                } catch (e: ActivityNotFoundException) {
                    // A deeplink to an external app was triggered, but there is no app installed to handle this deeplink
                    Snackbar.make(binding.root, R.string.in_app_browser_failed_opening_link, Snackbar.LENGTH_LONG).show()
                    true
                }
            }
        }
    }
}
```

> Note: There are various situations in the in-app browser flow where the user did not return to the app via the deeplink or callbacks.

## Step 4: Reload status in in-app browser Activity

Make sure to reload the payment status when opening the activity to detect whether the payment has been completed:

```kotlin
override fun onResume() {
    super.onResume()

    // Check if payment status changed and handle accordingly.
    // When the browser opened an external app, the external app will call our deeplink to open the app again. However there are some caveats:
    // 1. This screen remains in the stack if opening the deeplink did not close this activity (this could be resolved by using the CLEAR_TASK flag).
    // 2. The payment may be done asynchronously and the user may manually return to the app. In this case the last screen (which is this activity) is opened while the payment might have already been finished.
    // In conclusion it is a best practice to check the status of the payment when the user returns back to the app to make sure that this payment is not completed already.
    getPayment()?.id?.let { paymentId ->
        lifecycleScope.launch {
            val latestPayment = vm.getLatestPayment(paymentId)
            if (latestPayment?.status?.isCompleted == true) {
                Log.d("$javaClass", "Payment has been completed already, finishing")
                finish()
                Navigation.openPayments(this@InAppBrowserActivity, paymentId) // Refreshing the payments via `onNewIntent()`
            }
        }
    }
}
```

## Step 5: Reload status in result Activity

Make sure to check for the payment status in the other screens as well. In the demo app the in-app browser Activity launches the payments list so that we can handle it as if it came from the deeplink with updating our `checkCompletedPayment()` method:

```kotlin
private fun checkCompletedPayment(intent: Intent?) {
    if (intent == null) {
        // We don't need to check for this intent, it is not a completed payment for sure
        return
    }

    val isFromDeeplink = intent.data?.scheme == getString(R.string.deeplink_scheme_app)
    val completedPaymentId = intent.getIntExtra(EXTRA_COMPLETED_PAYMENT_ID, -1)
    val isFromCompletedPayment = completedPaymentId != -1

    when {
        isFromDeeplink -> {
            // Payment data when called from deeplink
            val paymentId = intent.data?.path?.substring(1) // `path` starts with a `/`, removing that
        }
        isFromCompletedPayment -> {
            // Payment data when called from a completed payment
            val paymentId = completedPaymentId
        }
        else -> {
            // Opened this screen without a completed payment
        }
    }
}
```

# Additions

After implementing the advanced flow, the following additions are available:

- [Optional: Implement payment methods](IMPLEMENT_PAYMENT_METHODS.md)

# Resources

Related samples in Mollie Checkout:

- [InAppBrowserActivity.kt](app/src/main/java/com/mollie/checkout/feature/inappbrowser/InAppBrowserActivity.kt): example activity with the WebView.
- [PaymentsActivity.kt](app/src/main/java/com/mollie/checkout/feature/payments/PaymentsActivity.kt): additional handling for a completed payment in this flow.
