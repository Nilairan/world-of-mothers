package com.madispace.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.madispace.core.database.converter.ListConverter
import com.madispace.core.database.dao.ProductDao
import com.madispace.core.database.dao.UserTokenDao
import com.madispace.core.database.entities.ProductEntity
import com.madispace.core.database.entities.SellerEntity
import com.madispace.core.database.entities.TokenEntity

@Database(entities = [ProductEntity::class, SellerEntity::class, TokenEntity::class], version = 1)
@TypeConverters(ListConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract val productDao: ProductDao
    abstract val userTokenDao: UserTokenDao

    companion object {
        private const val DATABASE_NAME = "world_of_mother_database"
        fun getInstance(applicationContext: Context): AppDatabase {
            return Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java,
                DATABASE_NAME
            ).build()
        }
    }
}
