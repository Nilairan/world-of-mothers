package com.madispace.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.madispace.core.common.Mapper
import com.madispace.domain.models.product.Product

@Entity(tableName = "Product")
data class ProductEntity(
    @PrimaryKey val id: Int,
    val userId: Int,
    val categoryId: Int,
    val name: String,
    val price: Double,
    val info: String,
    val material: String,
    val size: String,
    val status: String,
    val address: String,
    val img: String
)

object ProductEntityMapper : Mapper<Product, ProductEntity> {
    override fun map(item: Product): ProductEntity {
        return ProductEntity(
            id = item.id,
            userId = item.userId,
            categoryId = item.categoryId,
            name = item.name,
            price = item.price,
            info = item.info,
            material = item.material,
            size = item.size,
            status = item.status,
            address = item.address,
            img = item.img
        )
    }
}

object ProductMapper : Mapper<ProductEntity?, Product?> {
    override fun map(item: ProductEntity?): Product? {
        return item?.let {
            Product(
                id = item.id,
                userId = item.userId,
                categoryId = item.categoryId,
                name = item.name,
                price = item.price,
                info = item.info,
                material = item.material,
                size = item.size,
                status = item.status,
                address = item.address,
                img = item.img
            )
        } ?: run {
            null
        }
    }
}
