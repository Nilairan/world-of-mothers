package com.madispace.worldofmothers.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.madispace.domain.models.ProductShort
import com.madispace.worldofmothers.common.ObserveFragment
import com.madispace.worldofmothers.common.SimpleObserver
import com.madispace.worldofmothers.common.debounceClicks
import com.madispace.worldofmothers.common.getContext
import com.madispace.worldofmothers.databinding.FragmentProfileBinding
import com.madispace.worldofmothers.routing.RouterProvider
import com.madispace.worldofmothers.routing.Screens
import com.madispace.worldofmothers.ui.catalog.items.ProductItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 11/30/20
 */
class ProfileFragment : ObserveFragment(), RouterProvider {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileViewModel by viewModels()
    private val adapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel.addLifecycleObserver(lifecycle)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.root.visibility = View.GONE
        binding.editProfile.debounceClicks().subscribe { router.navigateTo(Screens.ChangeProfileScreen()) }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.floatingActionButton.setOnClickListener { router.navigateTo(Screens.NewProductScreen()) }
    }

    override fun initObservers() {
        binding.productList.layoutManager = GridLayoutManager(binding.getContext(), 2)
        binding.productList.adapter = adapter
        viewModel.isAuthorizedUserLiveData.observe(viewLifecycleOwner, object : SimpleObserver<Boolean>() {
            override fun onSuccess(data: Boolean) {
                if (data) {
                    binding.root.visibility = View.VISIBLE
                } else {
                    router.navigateTo(Screens.SignInScreen())
                }
            }
        })
        viewModel.userProductLiveData.observe(viewLifecycleOwner, object : SimpleObserver<List<ProductShort>>() {
            override fun onSuccess(data: List<ProductShort>) {
                adapter.clear()
                adapter.addAll(data.map { ProductItem(it) { router.navigateTo(Screens.ProductScreen()) } })
            }

            override fun onLoading(loading: Boolean) {
                binding.progressCircular.visibility = if (loading) View.VISIBLE else View.GONE
            }
        })
    }

    companion object {
        fun newInstance() = ProfileFragment()
    }
}