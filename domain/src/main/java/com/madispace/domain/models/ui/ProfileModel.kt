package com.madispace.domain.models.ui

import com.madispace.domain.models.product.ProductShort
import com.madispace.domain.models.user.Profile

data class ProfileModel(
        val profile: Profile,
        val products: List<ProductShort>
)