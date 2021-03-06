package com.madispace.worldofmothers.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.madispace.worldofmothers.common.ObserveFragment
import com.madispace.worldofmothers.common.getContext
import com.madispace.worldofmothers.databinding.FragmentProfileBinding
import com.madispace.worldofmothers.routing.RouterProvider
import com.madispace.worldofmothers.routing.Screens
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ProfileFragment : ObserveFragment<ProfileViewModel>(ProfileViewModel::class.java),
    RouterProvider {

    private lateinit var binding: FragmentProfileBinding
    private val adapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.root.visibility = View.GONE
        binding.editProfile.setOnClickListener { router.navigateTo(Screens.ChangeProfileScreen()) }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.floatingActionButton.setOnClickListener { router.navigateTo(Screens.NewProductScreen()) }
        binding.productList.layoutManager = GridLayoutManager(binding.getContext(), 2)
        binding.productList.adapter = adapter
    }

    override fun initObservers() {
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.viewStates().collect { state -> state?.let { bindState(state) } }
        }
//        viewModel.userProductLiveData.observe(
//            viewLifecycleOwner,
//            object : SimpleObserver<List<ProductShort>>() {
//                override fun onSuccess(data: List<ProductShort>) {
//                    adapter.clear()
////                adapter.addAll(data.map { ProductItem(it) { router.navigateTo(Screens.ProductScreen()) } })
//                }
//
//                override fun onLoading(loading: Boolean) {
//                    binding.progressCircular.visibility = if (loading) View.VISIBLE else View.GONE
//                }
//            })
    }

    private fun bindState(state: ProfileViewModel.ProfileState) {
        when (state) {
            is ProfileViewModel.ProfileState.OpenSignInScreen -> router.navigateTo(Screens.SignInScreen())
        }
    }

    companion object {
        fun newInstance() = ProfileFragment()
    }
}