package com.mollie.checkout.feature.payments.selectcheckout

import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.mollie.checkout.R
import com.mollie.checkout.data.model.Issuer
import com.mollie.checkout.data.model.Method
import com.mollie.checkout.data.model.SelectCheckoutLayoutStyle
import com.mollie.checkout.databinding.FragmentSelectMethodBinding
import com.mollie.checkout.feature.base.BaseFragment
import com.mollie.checkout.feature.payments.selectcheckout.list.SelectCheckoutListener
import com.mollie.checkout.feature.payments.selectcheckout.list.method.MethodsAdapter
import com.mollie.checkout.util.setVisible

class SelectMethodFragment : BaseFragment<FragmentSelectMethodBinding>(),
    SelectCheckoutListener {

    companion object {
        private const val GRID_SPAN_COUNT = 2
    }

    private val vm: SelectCheckoutViewModel by activityViewModels()

    override fun getLayout() = R.layout.fragment_select_method
    override fun getViewModel() = vm

    private val adapter = MethodsAdapter(this)

    override fun setup() {
        setupView()
        setupListeners()
        setupObservers()
    }

    private fun setupView() {
        binding.recyclerView.adapter = adapter
        binding.tabLayout.selectTab(binding.tabLayout.getTabAt(vm.style.index))
        switchLayout(vm.style)
    }

    private fun setupListeners() {
        binding.refreshLayout.setOnRefreshListener {
            vm.refresh()
        }
        binding.proceed.setOnClickListener {
            vm.proceed()
        }
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}

            override fun onTabSelected(tab: TabLayout.Tab?) {
                switchLayout(SelectCheckoutLayoutStyle.from(binding.tabLayout.selectedTabPosition))
            }
        })
    }

    private fun setupObservers() {
        vm.isLoading().observe(this) { loading ->
            binding.refreshLayout.isRefreshing = loading
        }
        vm.getMethods().observe(this) { methods ->
            adapter.show(methods, vm.style)
        }
        vm.getSelection().observe(this) { selection ->
            val selectedMethod = selection?.first
            val selectedIssuer = selection?.second
            adapter.setSelected(selectedMethod, selectedIssuer)

            val hasSelectedMethodIssuers = !selectedMethod?.issuers.isNullOrEmpty()
            val hasSelectedIssuer = selectedIssuer != null
            when {
                vm.style == SelectCheckoutLayoutStyle.LIST && hasSelectedMethodIssuers -> {
                    binding.footer.setVisible(hasSelectedIssuer)
                }
                else -> {
                    binding.footer.setVisible(selectedMethod != null)
                }
            }
        }
    }

    private fun switchLayout(style: SelectCheckoutLayoutStyle?) {
        vm.onSetLayout(style)
        binding.recyclerView.layoutManager = if (style == SelectCheckoutLayoutStyle.GRID) {
            GridLayoutManager(requireContext(), GRID_SPAN_COUNT)
        } else {
            LinearLayoutManager(requireContext())
        }
    }

    override fun onMethodClicked(method: Method) {
        vm.onSelectedMethod(method)
    }

    override fun onIssuerClicked(issuer: Issuer) {
        vm.onSelectedIssuer(issuer)
    }
}