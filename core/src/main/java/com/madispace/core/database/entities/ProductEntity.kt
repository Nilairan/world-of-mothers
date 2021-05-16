package com.madispace.core.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.madispace.core.common.Mapper
import com.madispace.domain.models.product.Product
import com.madispace.domain.models.product.Seller

@Entity(tableName = "product")
data class ProductEntity(
    @PrimaryKey val id: Int,
    val categoryId: Int,
    val name: String,
    val price: Double,
    val info: String,
    val material: String,
    val size: String,
    val status: String,
    val address: String,
    val gallery: List<String>,
    @Embedded val user: SellerEntity
)

object ProductEntityMapper : Mapper<Product, ProductEntity> {
    override fun map(item: Product): ProductEntity {
        return ProductEntity(
            id = item.id,
            categoryId = item.categoryId,
            name = item.name,
            price = item.price,
            info = item.info,
            material = item.material,
            size = item.size,
            status = item.status,
            address = item.address,
            gallery = item.gallery,
            user = SellerEntity(
                username = item.user.username,
                firstName = item.user.firstName,
                surname = item.user.surname,
                tel = item.user.tel,
                image = item.user.image,
                itemsCount = item.user.itemsCount
            )
        )
    }
}

object ProductMapper : Mapper<ProductEntity?, Product?> {
    override fun map(item: ProductEntity?): Product? {
        return item?.let {
            Product(
                id = item.id,
                categoryId = item.categoryId,
                name = item.name,
                price = item.price,
                info = item.info,
                material = item.material,
                size = item.size,
                status = item.status,
                address = item.address,
                gallery = item.gallery,
                user = Seller(
                    username = item.user.username,
                    firstName = item.user.firstName,
                    surname = item.user.surname,
                    tel = item.user.tel,
                    image = item.user.image,
                    itemsCount = item.user.itemsCount
                )
            )
        } ?: run {
            null
        }
    }
}
