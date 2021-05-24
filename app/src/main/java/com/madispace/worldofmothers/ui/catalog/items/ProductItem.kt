package com.madispace.worldofmothers.ui.catalog.items

import android.view.View
import androidx.core.view.isVisible
import com.madispace.domain.models.product.ProductShort
import com.madispace.worldofmothers.R
import com.madispace.worldofmothers.common.getContext
import com.madispace.worldofmothers.common.getPrice
import com.madispace.worldofmothers.common.loadPhoto
import com.madispace.worldofmothers.databinding.ItemProductBinding
import com.xwray.groupie.viewbinding.BindableItem

class ProductItem(
    private val product: ProductShort,
    private val clickToProduct: () -> Unit,
    private val isSelfItem: Boolean = false,
    private val deleteProductListener: (Int) -> Unit = {}
) : BindableItem<ItemProductBinding>() {

    override fun bind(viewBinding: ItemProductBinding, position: Int) {
        with(viewBinding) {
            productName.text = product.name
            priceText.text =
                viewBinding.getContext().getString(R.string.price, product.price.getPrice())
            cardView.setOnClickListener { clickToProduct() }
            productLogo.loadPhoto(product.imageUrl)
            deleteButton.isVisible = isSelfItem
            deleteButton.setOnClickListener { deleteProductListener.invoke(product.id) }
        }
    }

    override fun getLayout(): Int {
        return R.layout.item_product
    }

    override fun initializeViewBinding(view: View): ItemProductBinding {
        return ItemProductBinding.bind(view)
    }
}