package com.mollie.checkout.feature.inappbrowser

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.mollie.checkout.R
import com.mollie.checkout.data.model.Payment
import com.mollie.checkout.databinding.ActivityInAppBrowserBinding
import com.mollie.checkout.feature.Navigation
import com.mollie.checkout.feature.base.BaseActivity
import com.mollie.checkout.feature.payments.PaymentsActivity
import kotlinx.coroutines.launch

class InAppBrowserActivity : BaseActivity<ActivityInAppBrowserBinding>() {

    companion object {
        const val EXTRA_PAYMENT = "payment"
    }

    private val vm: InAppBrowserViewModel by viewModels()

    override fun getLayout() = R.layout.activity_in_app_browser
    override fun getViewModel() = vm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupView()
        setupListeners()
        loadData()
    }

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

    private fun loadData() {
        val url = getPayment()?.url
        if (url == null) {
            Log.d("$javaClass", "Launched without payment url, finishing")
            finish()
        } else {
            binding.webView.loadUrl(url)
        }
    }

    private fun setupView() {
        setTitle(R.string.in_app_browser_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        @SuppressLint("SetJavaScriptEnabled")
        binding.webView.settings.javaScriptEnabled = true // Needed for the Mollie and issuer sites to load
        binding.webView.settings.domStorageEnabled = true // Needed for some issuers to load that makes use HTML 5 specifications, such as Rabobank when generating QR codes
    }

    private fun setupListeners() {
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
    }

    private fun getPayment(): Payment? {
        return intent.getParcelableExtra(EXTRA_PAYMENT)
    }

    override fun onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}