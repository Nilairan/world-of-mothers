package com.madispace.core.database.dao

import androidx.room.*
import com.madispace.core.database.entities.TokenEntity

@Dao
interface UserTokenDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertToken(tokenEntity: TokenEntity)

    @Query("SELECT * FROM token")
    fun getToken(): TokenEntity?

    @Query("DROP TABLE token")
    fun clearTable()
}