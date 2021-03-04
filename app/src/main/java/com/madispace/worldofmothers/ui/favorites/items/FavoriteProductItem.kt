package com.madispace.worldofmothers.ui.favorites.items

import android.view.View
import com.madispace.domain.models.product.ProductShort
import com.madispace.worldofmothers.R
import com.madispace.worldofmothers.common.loadPhoto
import com.madispace.worldofmothers.databinding.ItemProductFavoriteBinding
import com.xwray.groupie.viewbinding.BindableItem

class FavoriteProductItem(
    private val product: ProductShort
) : BindableItem<ItemProductFavoriteBinding>() {

    override fun bind(viewBinding: ItemProductFavoriteBinding, position: Int) {
        viewBinding.itemProduct.productName.text = product.name
        viewBinding.itemProduct.priceText.text = product.price.toString()
        viewBinding.itemProduct.productLogo.loadPhoto(product.imageUrl)
    }

    override fun getLayout(): Int {
        return R.layout.item_product_favorite
    }

    override fun initializeViewBinding(view: View): ItemProductFavoriteBinding {
        return ItemProductFavoriteBinding.bind(view)
    }
}