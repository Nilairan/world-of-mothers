package com.madispace.worldofmothers.ui.profile

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.work.*
import by.kirich1409.viewbindingdelegate.viewBinding
import com.madispace.domain.models.product.ProductShort
import com.madispace.domain.models.user.Profile
import com.madispace.worldofmothers.R
import com.madispace.worldofmothers.common.ObserveFragment
import com.madispace.worldofmothers.common.getContext
import com.madispace.worldofmothers.common.launchWhenStarted
import com.madispace.worldofmothers.common.loadPhoto
import com.madispace.worldofmothers.databinding.FragmentProfileBinding
import com.madispace.worldofmothers.routing.RouterProvider
import com.madispace.worldofmothers.routing.Screens
import com.madispace.worldofmothers.ui.catalog.items.ProductItem
import com.madispace.worldofmothers.ui.loadphoto.dialog.ChooseLoadPhotoDialog
import com.madispace.worldofmothers.worker.UploadPhotoWorker
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.coroutines.flow.onEach

class ProfileFragment : ObserveFragment<ProfileViewModel>(
    ProfileViewModel::class.java,
    R.layout.fragment_profile
), RouterProvider {

    private val binding: FragmentProfileBinding by viewBinding()
    private val adapter = GroupAdapter<GroupieViewHolder>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            root.visibility = View.GONE
            editProfile.setOnClickListener { router.navigateTo(Screens.ChangeProfileScreen()) }
            floatingActionButton.setOnClickListener { router.navigateTo(Screens.NewProductScreen()) }
            productList.layoutManager = GridLayoutManager(binding.getContext(), 2)
            productList.adapter = adapter
            photoProfile.setOnClickListener {
                ChooseLoadPhotoDialog.newInstance().show(childFragmentManager, null)
            }
            initFragmentListener()
        }
    }

    private fun initFragmentListener() {
        childFragmentManager.setFragmentResultListener(
            ChooseLoadPhotoDialog.LOAD_MEDIA,
            viewLifecycleOwner
        ) { _, result ->
            (result.get(ChooseLoadPhotoDialog.URI) as Uri?)?.let { uri ->
                context?.let {
                    val file = DocumentFile.fromSingleUri(it, uri)
                    file?.let { documentFile ->
                        startWorker(documentFile, uri)
                    }
                }
            }
        }
    }

    private fun startWorker(file: DocumentFile, uri: Uri) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val uploadWorkRequest: OneTimeWorkRequest =
            OneTimeWorkRequestBuilder<UploadPhotoWorker>()
                .setInputData(
                    workDataOf(
                        UploadPhotoWorker.URI to uri.toString(),
                        UploadPhotoWorker.FILE_NAME to file.name,
                    )
                )
                .setConstraints(constraints)
                .build()

        context?.let {
            WorkManager.getInstance(it)
                .enqueue(uploadWorkRequest)

            WorkManager.getInstance(it)
                .getWorkInfoByIdLiveData(uploadWorkRequest.id)
                .observe(viewLifecycleOwner, { info ->
                    when (info.state) {
                        WorkInfo.State.FAILED -> Toast.makeText(
                            it,
                            getString(R.string.no_upload_photo),
                            Toast.LENGTH_SHORT
                        ).show()
                        WorkInfo.State.SUCCEEDED -> viewModel.obtainEvent(ProfileViewModel.ProfileEvent.Reload)
                        else -> return@observe
                    }
                })
        }
    }

    override fun initObservers() {
        viewModel.viewStates()
            .onEach { state -> state?.let { bindState(state) } }
            .launchWhenStarted(lifecycleScope)
        viewModel.viewActions()
            .onEach { action -> action?.let { bindAction(action) } }
            .launchWhenStarted(lifecycleScope)
    }

    private fun bindState(state: ProfileViewModel.ProfileState) {
        when (state) {
            is ProfileViewModel.ProfileState.OpenSignInScreen -> router.navigateTo(Screens.SignInScreen())
            is ProfileViewModel.ProfileState.ShowLoading -> binding.progressCircular.isVisible =
                true
            is ProfileViewModel.ProfileState.HideLoading -> binding.progressCircular.isVisible =
                false
            is ProfileViewModel.ProfileState.ShowProfileData -> {
                showProfile(state.profile)
                showProduct(state.product)
            }
        }
    }

    private fun bindAction(action: ProfileViewModel.ProfileAction) {
    }

    private fun showProfile(profile: Profile) {
        with(binding) {
            root.isVisible = true
            profileName.text = profile.getName()
            profileDescription.text = profile.getDescription()
            photoProfile.loadPhoto(profile.image)
        }
    }

    private fun showProduct(products: List<ProductShort>) {
        binding.root.isVisible = true
        binding.emptyText.isVisible = products.isEmpty()
        adapter.clear()
        adapter.addAll(products.map {
            ProductItem(it) { router.navigateTo(Screens.ProductScreen(it.id)) }
        })
    }

    companion object {
        fun newInstance() = ProfileFragment()
    }
}