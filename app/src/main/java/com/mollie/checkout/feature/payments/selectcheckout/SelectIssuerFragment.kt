package com.mollie.checkout.feature.payments.selectcheckout

import androidx.fragment.app.activityViewModels
import com.mollie.checkout.R
import com.mollie.checkout.data.model.Issuer
import com.mollie.checkout.data.model.Method
import com.mollie.checkout.data.model.SelectCheckoutLayoutStyle
import com.mollie.checkout.databinding.FragmentSelectIssuerBinding
import com.mollie.checkout.feature.base.BaseFragment
import com.mollie.checkout.feature.payments.selectcheckout.list.SelectCheckoutListener
import com.mollie.checkout.feature.payments.selectcheckout.list.issuer.IssuersAdapter
import com.mollie.checkout.util.setVisible

/**
 * <b>Note:</b> This fragment is only used when the user chooses a payment method via the grid layout.
 */
class SelectIssuerFragment : BaseFragment<FragmentSelectIssuerBinding>(), SelectCheckoutListener {

    private val vm: SelectCheckoutViewModel by activityViewModels()

    override fun getLayout() = R.layout.fragment_select_issuer
    override fun getViewModel() = vm

    private val adapter = IssuersAdapter(SelectCheckoutLayoutStyle.GRID)

    override fun setup() {
        setupView()
        setupObservers()
    }

    private fun setupView() {
        binding.recyclerView.adapter = adapter
        binding.proceed.setOnClickListener {
            vm.proceed()
        }
    }

    private fun setupObservers() {
        vm.getSelection().observe(viewLifecycleOwner) { selection ->
            val selectedMethod = selection?.first
            val selectedIssuer = selection?.second
            adapter.show(selectedMethod?.issuers, selectedIssuer, this)
            binding.footer.setVisible(selection?.second != null)
        }
    }

    override fun onMethodClicked(method: Method) {
        // Not triggered on select issuer
    }

    override fun onIssuerClicked(issuer: Issuer) {
        vm.onSelectedIssuer(issuer)
    }
}