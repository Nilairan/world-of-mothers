package com.madispace.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.madispace.core.database.entities.ProductEntity

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(vararg product: ProductEntity)

    @Query("SELECT * FROM Product WHERE id = :id")
    suspend fun getById(id: Int): ProductEntity?

    @Query("DELETE FROM Product WHERE id = :id")
    suspend fun removeById(id: Int)
}