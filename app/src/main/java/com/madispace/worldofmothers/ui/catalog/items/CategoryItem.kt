package com.madispace.worldofmothers.ui.catalog.items

import android.view.View
import com.madispace.domain.models.category.Category
import com.madispace.worldofmothers.R
import com.madispace.worldofmothers.databinding.ItemCategoryBinding
import com.xwray.groupie.viewbinding.BindableItem

/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 12/2/20
 */
class CategoryItem(
    private val category: Category,
    private val clickListener: () -> Unit
) : BindableItem<ItemCategoryBinding>() {
    override fun bind(viewBinding: ItemCategoryBinding, position: Int) {
        viewBinding.categoryName.text = category.name
        viewBinding.root.setOnClickListener { clickListener() }
    }

    override fun getLayout(): Int {
        return R.layout.item_category
    }

    override fun initializeViewBinding(view: View): ItemCategoryBinding {
        return ItemCategoryBinding.bind(view)
    }
}