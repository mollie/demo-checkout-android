# Source explained

## Settings

The demo app comes with a few configured settings in [Settings.kt](app/src/main/java/com/mollie/checkout/Settings.kt). These settings can be modified to check out the flow when running the demo app.

We recommend trying out the different options of the available settings when running the app to get a feeling about which flow is preferred in your case.

| Setting | Description |
| ------- | ----------- |
| `Settings.Network.BASE_URL` | The base url of the backend in use. |
| `Settings.Navigation.SELECT_PAYMENT_METHOD` | Indicates whether to select the payment method directly when creating a payment. <br> This value defines how **creating** and **executing** the payment works in the demo app. |
| `Settings.Navigation.PAYMENT_FLOW` | Defines the flow that is used when executing a payment. |

## Networking

In this demo app we use Retrofit to communicate to the backend:

- [Connectivity.kt](app/src/main/java/com/mollie/checkout/data/net/Connectivity.kt): Retrofit configuration
- [ApiService.kt](app/src/main/java/com/mollie/checkout/data/net/ApiService.kt): Definition of backend calls

Within the ApiService there are 4 calls:

- `createPayment(payment: Payment)`: Used to create a new payment.
- `getPayments()`: Used to retrieve the list of payments.
- `getPayment(id: Int)`: Only used to retrieve a single payment.
- `getMethods()`: Only needed when `Settings.Navigation.SELECT_PAYMENT_METHOD` is `true`.

# User flow

## 1: Splash

The [SplashActivity.kt](app/src/main/java/com/mollie/checkout/feature/splash/SplashActivity.kt) retrieves the payments list and proceeds when these are loaded.

## 2: List payments

In [PaymentsActivity.kt](app/src/main/java/com/mollie/checkout/feature/payments/PaymentsActivity.kt) the payments are retrieved and shown.

> **Note:** Refreshing the payments list in the `onResume()` method is recommended to make sure the user always sees the latest state of their payments.

## 3: Create payment

The bare minimum needed to create a payment is a `description` and the `amount`. Usually these values are determined based on what is being bought. For demonstration purposes, in [CreatePaymentActivity.kt](app/src/main/java/com/mollie/checkout/feature/payments/create/CreatePaymentActivity.kt) the user provides these values.

Next when clicking **Create** button, the app continues depending on `Settings.Navigation.SELECT_PAYMENT_METHOD`:

- When `true`, see [3A](#3a-optional-selecting-the-payment-method)
- When `false`, see [3B](#3b-payment-saved)

> **Note:** Selecting the payment method when creating the payment is completely optional. Creating a payment only with the `description` and `amount` will allow the user to select the payment method later when executing the payment.

### 3A Optional: Selecting the payment method

In [SelectCheckoutActivity.kt](app/src/main/java/com/mollie/checkout/feature/payments/selectcheckout/SelectCheckoutActivity.kt) the payment method selection is implemented natively. In case the payment method has issuers, selecting the issuer is also required. When selecting **Continue**, the app proceeds to **3B**.

The demo app provides a tab selection on top to switch between the list and grid layout for selecting the method and issuer. You can choose which layout is preferred in your app.

> **Note:** [SelectIssuerFragment](app/src/main/java/com/mollie/checkout/feature/payments/selectcheckout/SelectIssuerFragment.kt) is only used when using the grid layout. 

### 3B: Payment saved

The payment is saved with the values, proceeding to the next step: executing the payment.

## 4: Executing the payment

The demo app determines the payment flow based on `Settings.Navigation.PAYMENT_FLOW`:

- When `PaymentFlow.EXTERNAL_BROWSER`, see [4A](#4a-external-browser-flow)
- When `PaymentFlow.IN_APP_BROWSER`, see [4B](#4b-in-app-browser-flow)
- When `PaymentFlow.CHOOSE`, see [4C](#4c-choose-flow)

### 4A: External browser flow

The url is opened via Chrome Custom Tabs to allow a bit of customization when opening the url. Checkout the method `openUrl()` in [Navigation.kt](app/src/main/java/com/mollie/checkout/feature/Navigation.kt).

> **Note:** Although Chrome Custom Tabs allow for some customization, the device might still launch a different browser based on the user's device preferences. This means that then the customized values can be ignored depending on the browser that is being launched.

### 4B: In-app browser flow

The url is opened within a WebView inside [InAppBrowserActivity.kt](app/src/main/java/com/mollie/checkout/feature/inappbrowser/InAppBrowserActivity.kt). The WebView settings are set to successfully load the payment pages. 

The WebViewClient is used to handle the callbacks from the WebView. Overriding `shouldOverrideUrlLoading()` is **required** to handle called deeplinks from the WebView as well as universal links that should be opened in the native apps. 

### 4C: Choose flow

With this setting the app will show an alert, giving the user the choice to use the **5A: External browser flow** or the **5B: In-app browser flow**.

## 5: Payment result

The [PaymentsActivity.kt](app/src/main/java/com/mollie/checkout/feature/payments/PaymentsActivity.kt) reloads the payment in `onResume()` to ensure that the latest state of the payments are shown.

Additionally, this activity is configured to handle the result of the deeplinks. In the `checkCompletedPayment()` method the payment id is retrieved from the deeplink when a payment completed, regardless of the payment status.

### 5A: In-app browser flow

When using the in-app browser flow, the [InAppBrowserActivity.kt](app/src/main/java/com/mollie/checkout/feature/inappbrowser/InAppBrowserActivity.kt) also refreshes the state of the payment in `onResume()`. This is because with the in-app browser flow, there are various cases where the payment is completed but did not return to that activity or to the app.
