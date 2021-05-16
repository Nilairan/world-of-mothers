package com.madispace.core.network.dto.user

import com.google.gson.annotations.SerializedName
import com.madispace.domain.models.user.Profile

data class DTOProfile(
        val username: String,
        @SerializedName("first_name") val firstName: String,
        val surname: String,
        val tel: String,
        val image: String,
        @SerializedName("items_count") val itemsCount: String
) {
    fun mapToModel(): Profile {
        return Profile(username, firstName, surname, tel, image, itemsCount)
    }
}
