package com.madispace.worldofmothers.ui.createproduct

import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.madispace.domain.models.image.PhotoModel
import com.madispace.worldofmothers.R
import com.madispace.worldofmothers.common.ObserveFragment
import com.madispace.worldofmothers.common.doAfterTextChanged
import com.madispace.worldofmothers.common.getContext
import com.madispace.worldofmothers.common.launchWhenStarted
import com.madispace.worldofmothers.databinding.FragmentNewProductBinding
import com.madispace.worldofmothers.ui.createproduct.item.PhotoViewHolder
import com.madispace.worldofmothers.ui.loadphoto.dialog.ChooseLoadPhotoDialog
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.coroutines.flow.onEach

class CreateProductFragment : ObserveFragment<CreateProductViewModel>(
    CreateProductViewModel::class.java,
    R.layout.fragment_new_product
) {

    private val binding: FragmentNewProductBinding by viewBinding()
    private val photoAdapter = GroupAdapter<GroupieViewHolder>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            appBar.toolbarName.text = getString(R.string.add_product_title)
            addPhotoButton.setOnClickListener {
                ChooseLoadPhotoDialog.newInstance().show(childFragmentManager, null)
            }
            addProduct.setOnClickListener {
                viewModel.obtainEvent(CreateProductViewModel.CreateProductEvent.AddProduct)
            }
            imageContainer.adapter = photoAdapter

        }
        initFragmentListener()
        addInputListeners()
    }

    private fun initFragmentListener() {
        childFragmentManager.setFragmentResultListener(
            ChooseLoadPhotoDialog.LOAD_MEDIA,
            viewLifecycleOwner
        ) { _, result ->
            (result.get(ChooseLoadPhotoDialog.URI) as Uri?)?.let { uri ->
                val mediaType = context?.contentResolver?.getType(uri) ?: ""
                val file = context?.contentResolver?.openInputStream(uri)?.readBytes()
                file?.let {
                    viewModel.obtainEvent(
                        CreateProductViewModel.CreateProductEvent.AddPhoto(
                            uri,
                            PhotoModel(
                                file,
                                mediaType,
                                file.toString()
                            )
                        )
                    )
                }
            }
        }
    }

    private fun addInputListeners() {
        with(binding) {
            nameLayout.doAfterTextChanged {
                viewModel.obtainEvent(CreateProductViewModel.CreateProductEvent.SetName(it))
            }
            stateLayout.doAfterTextChanged {
                viewModel.obtainEvent(CreateProductViewModel.CreateProductEvent.SetStatus(it))
            }
            materialLayout.doAfterTextChanged {
                viewModel.obtainEvent(CreateProductViewModel.CreateProductEvent.SetMaterial(it))
            }
            sizeLayout.doAfterTextChanged {
                viewModel.obtainEvent(CreateProductViewModel.CreateProductEvent.SetSize(it))
            }
            costLayout.doAfterTextChanged {
                val price = it.toIntOrNull() ?: 0
                viewModel.obtainEvent(CreateProductViewModel.CreateProductEvent.SetPrice(price))
            }
            addressLayout.doAfterTextChanged {
                viewModel.obtainEvent(CreateProductViewModel.CreateProductEvent.SetAddress(it))
            }
            descriptionLayout.doAfterTextChanged {
                viewModel.obtainEvent(CreateProductViewModel.CreateProductEvent.SetDescription(it))
            }
        }
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
            is CreateProductViewModel.CreateProductState.ShowPhotos -> showPhotos(state.listUri)
        }
    }

    private fun bindAction(action: CreateProductViewModel.CreateProductAction) {
        with(binding) {
            when (action) {
                is CreateProductViewModel.CreateProductAction.ShowError -> showError(action.error)
                is CreateProductViewModel.CreateProductAction.ShowCategories -> showCategories(
                    action.categories
                )
                is CreateProductViewModel.CreateProductAction.SuccessAddProduct -> {
                    showError(getString(R.string.success_add_product))
                    router.exit()
                }
                is CreateProductViewModel.CreateProductAction.NoValidName ->
                    nameLayout.error = getString(R.string.field_not_valid)
                is CreateProductViewModel.CreateProductAction.NoValidStatus ->
                    stateLayout.error = getString(R.string.field_not_valid)
                is CreateProductViewModel.CreateProductAction.NoValidMaterial ->
                    materialLayout.error = getString(R.string.field_not_valid)
                is CreateProductViewModel.CreateProductAction.NoValidSize ->
                    sizeLayout.error = getString(R.string.field_not_valid)
                is CreateProductViewModel.CreateProductAction.NoValidAddress ->
                    addressLayout.error = getString(R.string.field_not_valid)
                is CreateProductViewModel.CreateProductAction.NoValidDescription ->
                    descriptionLayout.error = getString(R.string.field_not_valid)
                is CreateProductViewModel.CreateProductAction.NoValidPrice ->
                    costLayout.error = getString(R.string.price_not_valid)
                is CreateProductViewModel.CreateProductAction.NoValidCategory ->
                    categoryLayout.error = getString(R.string.field_not_valid)
                is CreateProductViewModel.CreateProductAction.NoValidPhoto ->
                    Toast.makeText(
                        getContext(),
                        getString(R.string.photo_not_valid),
                        Toast.LENGTH_LONG
                    ).show()
            }
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

    private fun showPhotos(list: List<Uri>) {
        context?.let { context ->
            photoAdapter.clear()
            photoAdapter.addAll(list.map {
                PhotoViewHolder(
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        it to ImageDecoder.decodeBitmap(
                            ImageDecoder.createSource(
                                context.contentResolver,
                                it
                            )
                        )
                    } else {
                        it to MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                    }
                ) {
                    viewModel.obtainEvent(CreateProductViewModel.CreateProductEvent.RemovePhoto(it))
                }
            })
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