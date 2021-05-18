package com.madispace.worldofmothers.ui.createproduct

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.madispace.worldofmothers.R
import com.madispace.worldofmothers.common.ObserveFragment
import com.madispace.worldofmothers.common.launchWhenStarted
import com.madispace.worldofmothers.databinding.FragmentNewProductBinding
import kotlinx.coroutines.flow.onEach

class CreateProductFragment : ObserveFragment<CreateProductViewModel>(
    CreateProductViewModel::class.java,
    R.layout.fragment_new_product
) {

    private val binding: FragmentNewProductBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.appBar.toolbarName.text = getString(R.string.add_product_title)
    }

    override fun initObservers() {
        viewModel.viewStates()
            .onEach { state -> state?.let { bindState(it) } }
            .launchWhenStarted(lifecycleScope)
        viewModel.viewActions()
            .onEach { action -> action?.let { bindAction(action) } }
            .launchWhenStarted(lifecycleScope)
    }

    private fun bindState(state: CreateProductViewModel.CreateProductState) {
        when (state) {
            is CreateProductViewModel.CreateProductState.Loading -> binding.progressBar.root.isVisible =
                state.loading
        }
    }

    private fun bindAction(action: CreateProductViewModel.CreateProductAction) {
        when (action) {
            is CreateProductViewModel.CreateProductAction.ShowError -> showError(action.error)
            is CreateProductViewModel.CreateProductAction.ShowCategories -> showCategories(action.categories)
        }
    }

    private fun showCategories(categories: List<String>) {
        val adapter = ArrayAdapter(requireContext(), R.layout.item_list, categories)
        (binding.categoryLayout.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        (binding.categoryLayout.editText as? AutoCompleteTextView)?.setOnItemClickListener { _, _, position, _ ->
            viewModel.obtainEvent(
                CreateProductViewModel.CreateProductEvent.SelectCategory(
                    categories[position]
                )
            )
        }
    }

    private fun showError(error: String) {
        context?.let {
            Toast.makeText(it, error, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        fun newInstance() = CreateProductFragment()
    }
}