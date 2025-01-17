package com.madispace.worldofmothers.ui.product

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.appbar.AppBarLayout
import com.madispace.domain.models.product.Product
import com.madispace.domain.models.product.Seller
import com.madispace.worldofmothers.R
import com.madispace.worldofmothers.common.*
import com.madispace.worldofmothers.databinding.FragmentProductBinding
import com.madispace.worldofmothers.routing.Screens
import com.madispace.worldofmothers.ui.catalog.items.ProductItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.coroutines.flow.onEach


class ProductFragment : ObserveFragment<ProductViewModel>(
    ProductViewModel::class.java,
    R.layout.fragment_product
) {

    private val binding: FragmentProductBinding by viewBinding()
    private var productId: Int by args()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (requireActivity() is AppCompatActivity) {
            (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        }
        binding.recommendedProductList.layoutManager =
                LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        viewModel.obtainEvent(ProductViewModel.ProductEvent.LoadProduct(productId))
        initToolbar()
        binding.fab.setOnClickListener {
            viewModel.obtainEvent(ProductViewModel.ProductEvent.OnFavoriteClick)
        }
    }

    private fun initToolbar() {
        with(binding) {
            collapsingToolbar.isTitleEnabled = false
            toolbar.title = ""
            toolbar.setNavigationIcon(R.drawable.ic_back)
            toolbar.setNavigationOnClickListener {
                onBackPressed()
            }
            appBar.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
                override fun onStateChanged(appBarLayout: AppBarLayout?, state: State?) {
                    when (state) {
                        State.COLLAPSED -> {
                            toolbar.setNavigationIcon(R.drawable.ic_back)
                        }
                        State.EXPANDED -> {
                            toolbar.setNavigationIcon(R.drawable.ic_back_white)
                        }
                        else -> {
                        }
                    }
                }
            })
        }
    }

    override fun initObservers() {
        viewModel.viewStates().onEach { state ->
            state?.let { bindViewState(state) }
        }.launchWhenStarted(lifecycleScope)
    }

    private fun bindViewState(state: ProductViewModel.ProductState) {
        with(binding) {
            when (state) {
                is ProductViewModel.ProductState.ShowLoading -> {
                    progressBar.root.visibility = View.VISIBLE
                    fab.visibility = View.GONE
                }
                is ProductViewModel.ProductState.HideLoading -> {
                    progressBar.root.visibility = View.GONE
                    fab.visibility = View.VISIBLE
                }
                is ProductViewModel.ProductState.ShowProduct -> {
                    bindProduct(state.product)
                }
                is ProductViewModel.ProductState.ShowSeller -> {
                    bindSeller(state.seller)
                }
                is ProductViewModel.ProductState.ShowAdditionallyProduct -> {
                    binding.recommendedProductList.adapter =
                        GroupAdapter<GroupieViewHolder>().apply {
                            addAll(state.additionallyProduct.map {
                                ProductItem(
                                    it,
                                    { router.replaceScreen(Screens.ProductScreen(it.id)) })
                            })
                        }
                }
                is ProductViewModel.ProductState.EnableFavorite -> {
                    fab.setImageDrawable(
                        ContextCompat.getDrawable(
                            getContext(),
                            R.drawable.ic_like_green
                        )
                    )
                }
                is ProductViewModel.ProductState.DisableFavorite -> {
                    fab.setImageDrawable(
                        ContextCompat.getDrawable(
                            getContext(),
                            R.drawable.ic_favorite_24
                        )
                    )
                }
            }
        }
    }

    private fun bindProduct(product: Product) {
        with(binding) {
            activity?.let {
                val imageAdapter = ScreenSlidePagerAdapter(it, product.gallery)
                imageProductViewPager.adapter = imageAdapter
                indicator.attachToPager(imageProductViewPager)
            }
            nameProduct.text = product.name
            costProduct.text = getString(R.string.price, product.price.getPrice())
            descriptionProduct.text = product.info
            materialText.text = product.material
            sizeText.text = product.size
            stateText.text = product.status
            deliveryText.text = product.address
        }
    }

    private fun bindSeller(seller: Seller) {
        with(binding) {
            nameSeller.text = "${seller.surname} ${seller.firstName}"
            countProduct.text = getContext().resources.getQuantityString(
                R.plurals.advertisement,
                seller.itemsCount,
                seller.itemsCount
            )
            imageSeller.loadPhoto(seller.image)
            callButton.setOnClickListener {
                val intent = Intent(
                    Intent.ACTION_DIAL,
                    Uri.parse("tel:" + PhoneNumberUtils.formatNumber(seller.getPhone(), "RU"))
                )
                startActivity(intent)
            }
        }
    }

    companion object {
        fun newInstance(productId: Int) = ProductFragment().apply {
            this.productId = productId
        }
    }
}