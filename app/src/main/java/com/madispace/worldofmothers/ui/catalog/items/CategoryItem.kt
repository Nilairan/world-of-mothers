package com.madispace.worldofmothers.ui.catalog.items

import android.view.View
import androidx.core.content.ContextCompat
import com.madispace.domain.models.category.Category
import com.madispace.worldofmothers.R
import com.madispace.worldofmothers.common.getContext
import com.madispace.worldofmothers.databinding.ItemCategoryBinding
import com.xwray.groupie.viewbinding.BindableItem

class CategoryItem(
    private val category: Category,
    private val clickListener: (Category) -> Unit
) : BindableItem<ItemCategoryBinding>() {
    override fun bind(viewBinding: ItemCategoryBinding, position: Int) {
        with(viewBinding) {
            categoryName.text = category.name
            root.setOnClickListener { clickListener.invoke(category) }
            when (category.name) {
                getContext().getString(R.string.babes) ->
                    categoryImage.setImageDrawable(
                        ContextCompat.getDrawable(
                            getContext(),
                            R.drawable.ic_babes
                        )
                    )
                getContext().getString(R.string.before_3) ->
                    categoryImage.setImageDrawable(
                        ContextCompat.getDrawable(
                            getContext(),
                            R.drawable.ic_before_3
                        )
                    )
                getContext().getString(R.string.before_7) ->
                    categoryImage.setImageDrawable(
                        ContextCompat.getDrawable(
                            getContext(),
                            R.drawable.ic_before_8
                        )
                    )
                getContext().getString(R.string.before_18) ->
                    categoryImage.setImageDrawable(
                        ContextCompat.getDrawable(
                            getContext(),
                            R.drawable.ic_before_18
                        )
                    )
                getContext().getString(R.string.pregnant) ->
                    categoryImage.setImageDrawable(
                        ContextCompat.getDrawable(
                            getContext(),
                            R.drawable.ic_pregnant
                        )
                    )
            }
        }
    }

    override fun getLayout(): Int {
        return R.layout.item_category
    }

    override fun initializeViewBinding(view: View): ItemCategoryBinding {
        return ItemCategoryBinding.bind(view)
    }
}