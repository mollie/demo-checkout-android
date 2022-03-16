# Getting started

This page guides you through the steps you need to get started with Mollie Checkout for Android.

## Set up a backend

Mollie payments must be handled securely through a backend. The backend processes the following functionalities:

-   Create a payment.
-   Retrieve a payment.
-   Retrieve payment history (optional). Useful to show the payment status in case customers need to relaunch the app.
-   Retrieve payment methods (optional). Only applies when customising the payment method selection.

To set up your backend:

1.  Go to [Mollie Checkout Backend](https://github.com/mollie/demo-checkout-backend).
2.  Follow the steps in [README.md](https://github.com/mollie/demo-checkout-backend#readme) to set up the backend.

You have now set up the backend that you need to execute secure calls from your app.

## Link the app to your backend

To run the app, point the base URL to your backend environment.

1.  Open **app** → **src** → **main** → **java** → **com** → **mollie** → **checkout** → [Settings.kt](app/src/main/java/com/mollie/checkout/Settings.kt).
2.  Point the `BASE_URL` to your environment.

The app now communicates with your environment to securely handle backend calls.

## Run the app

We recommend you to run this demo app to see how it works.

1.  Read [about the source](SOURCE.md) to understand the important source files and settings.
2.  Run the [basic implementation flow](FLOW_BASIC.md).

This enables you to execute a payment flow in your app. If you want to customise the payment flows in your app, you can run the [advanced implementation flow](FLOW_ADVANCED.md) and/or include the [payment method selection step](IMPLEMENT_PAYMENT_METHODS.md) in your app.
