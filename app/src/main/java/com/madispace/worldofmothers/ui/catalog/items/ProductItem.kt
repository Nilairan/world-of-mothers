package com.madispace.worldofmothers.ui.catalog.items

import android.view.View
import com.madispace.domain.models.ProductShort
import com.madispace.worldofmothers.R
import com.madispace.worldofmothers.databinding.ItemProductBinding
import com.xwray.groupie.viewbinding.BindableItem

/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 12/2/20
 */
class ProductItem(
        private val product: ProductShort
) : BindableItem<ItemProductBinding>() {

    override fun bind(viewBinding: ItemProductBinding, position: Int) {
        viewBinding.productName.text = product.name
        viewBinding.priceText.text = product.price.toString()
    }

    override fun getLayout(): Int {
        return R.layout.item_product
    }

    override fun initializeViewBinding(view: View): ItemProductBinding {
        return ItemProductBinding.bind(view)
    }
}