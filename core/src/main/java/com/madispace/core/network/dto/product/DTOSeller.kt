package com.madispace.core.network.dto.product

import com.google.gson.annotations.SerializedName
import com.madispace.domain.models.product.Seller

data class DTOSeller(
    val username: String,
    @SerializedName("first_name") val firstName: String,
    val surname: String,
    val tel: String,
    val image: String,
    @SerializedName("items_count") val itemsCount: Int
) {
    fun mapToModel(): Seller {
        return Seller(
            username,
            firstName,
            surname,
            tel,
            image,
            itemsCount
        )
    }
}
