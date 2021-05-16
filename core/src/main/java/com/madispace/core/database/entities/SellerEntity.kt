package com.madispace.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "seller")
data class SellerEntity(
    @PrimaryKey val username: String,
    val firstName: String,
    val surname: String,
    val tel: String,
    val image: String,
    val itemsCount: Int
)
