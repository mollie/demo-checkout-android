# About the source

This page takes you through some of the source files you need to execute payments in your app.

## Configuration

### Basic settings

You can modify the app’s basic settings in [Settings.kt](app/src/main/java/com/mollie/checkout/Settings.kt).

-   `Settings.Network.BASE_URL` specifies the link used to communicate with your backend. This is needed to securely handle the API calls when executing the payment flow.
-   `Settings.Navigation.SELECT_PAYMENT_METHOD` indicates whether to use a custom payment method selection step.
    -   `false` is the default setting. Customers select the payment method in the web when they execute the payment. 
    -   `true` enables you to customise the payment method selection. Customers select the payment method in your app before executing the payment in the web.
-   `Settings.Navigation.PAYMENT_FLOW` defines how to execute the payment.
    -   `PaymentFlow.CHOOSE` displays an alert for the customer to choose their preferred flow.
    -   `PaymentFlow.EXTERNAL_BROWSER` uses the [basic implementation flow](FLOW_BASIC.md).
    -   `PaymentFlow.IN_APP_BROWSER` uses the [advanced implementation flow](FLOW_ADVANCED.md).

> :white_check_mark: **Tip**: Run the app using different settings to discover your preferred implementation.

### Network and backend calls

The demo app uses Retrofit to communicate with the backend and execute the API calls needed for the payment flows.

-   [Connectivity.kt](app/src/main/java/com/mollie/checkout/data/net/Connectivity.kt) contains the Retrofit configuration.  
-   [ApiService.kt](app/src/main/java/com/mollie/checkout/data/net/ApiService.kt) contains the backend calls:
    -   `createPayment(payment: Payment)` creates a payment.
    -   `getPayments()` retrieves a list of payments.
    -   `getPayment(id: Int)` retrieves the specified payment.
    -   `getMethods()` retrieves the payment methods. Only applies when `Settings.Navigation.SELECT_PAYMENT_METHOD` is `true`.

## App functionalities

The demo app uses various source files to handle its functionalities.

### Retrieve payments

There are two files that the app uses to retrieve payments.

[SplashActivity.kt](app/src/main/java/com/mollie/checkout/feature/splash/SplashActivity.kt) displays a loading screen while it retrieves the payments list. When it’s finished loading, it displays the payments list.
    
[PaymentsActivity.kt](app/src/main/java/com/mollie/checkout/feature/payments/PaymentsActivity.kt) retrieves and displays the payments list.

> :warning: **Note**: To ensure the latest payment statuses are shown, refresh the payments list using the `onResume()` method in [PaymentsActivity.kt](app/src/main/java/com/mollie/checkout/feature/payments/PaymentsActivity.kt). This way, customers can see whether their payments were successful.

### Create payment

The API call to create a payment requires a `description` and an `amount`. In general, these values are determined by the ordered item. For demonstration purposes, the customer provides these values in [CreatePaymentActivity.kt](app/src/main/java/com/mollie/checkout/feature/payments/create/CreatePaymentActivity.kt).

When the customer taps **Create**, the app continues to the select payment method step if `Settings.Navigation.SELECT_PAYMENT_METHOD` in [Settings.kt](app/src/main/java/com/mollie/checkout/Settings.kt) is `true`. Otherwise, it saves the payment and proceeds to [the execution step](#execute-payment).

### Select payment method (optional)

If you customise the payment selection step, the app continues to the payment method on **Create**.

You can choose whether to display the payment methods and issuers in a list or a grid layout.

-   [SelectCheckoutActivity.kt](app/src/main/java/com/mollie/checkout/feature/payments/selectcheckout/SelectCheckoutActivity.kt) implements the payment method selection natively.
-   [SelectIssuerFragment](app/src/main/java/com/mollie/checkout/feature/payments/selectcheckout/SelectIssuerFragment.kt) only applies when the customer selects a payment method through the grid layout.

### Execute payment

The app executes the payment according to the `Settings.Navigation.PAYMENT_FLOW` in [Settings.kt](app/src/main/java/com/mollie/checkout/Settings.kt).

> :white_check_mark: **Tip**: You can implement both flows and set `Settings.Navigation.PAYMENT_FLOW` to `PaymentFlow.CHOOSE`. In this case, the app displays an alert that enables customers to choose whether to continue in the app or in their browser.

#### Basic implementation

In the basic implementation, the payment link opens in an external browser on the customer’s device.

To implement this flow, set `Settings.Navigation.PAYMENT_FLOW` to `PaymentFlow.EXTERNAL_BROWSER`.

[Navigation.kt](app/src/main/java/com/mollie/checkout/feature/Navigation.kt) contains the `openUrl()` method. You can customise some elements for Chrome Custom Tabs in this method.

> :warning: **Note**: Other browsers might ignore custom values.

#### Advanced implementation

In the advanced implementation, the payment link opens in a WebView inside the app.

To implement this flow, set `Settings.Navigation.PAYMENT_FLOW` to `PaymentFlow.IN_APP_BROWSER`.

To access the WebView sample files, open **app** → **src** → **main** → **java** → **com** → **mollie** → **checkout** → **feature** → **inappbrowser**.

[InAppBrowserActivity.kt](app/src/main/java/com/mollie/checkout/feature/inappbrowser/InAppBrowserActivity.kt) enables you to configure the settings needed to successfully load payment pages. The WebViewClient is used to handle callbacks from the WebView.

> :warning: **Note**: You must override `shouldOverrideUrlLoading()`. This is required to handle deep links called from the WebView and universal links that launch native apps.

### Handle payment result

[PaymentsActivity.kt](app/src/main/java/com/mollie/checkout/feature/payments/PaymentsActivity.kt) contains two methods that handle a processed payment:
-   `onResume()` refreshes the payments list to show the latest status.
-   `checkCompletedPayment()` retrieves the payment ID from the deep link after a payment is complete, regardless of its status.

In the advanced implementation, there are various cases in which a customer completes their payment but isn’t returned to the Activity or the app. [InAppBrowserActivity.kt](app/src/main/java/com/mollie/checkout/feature/inappbrowser/InAppBrowserActivity.kt) therefore also contains the `onResume()` method to refresh the payments.
