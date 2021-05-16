package com.madispace.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.madispace.core.database.entities.TokenEntity

@Dao
interface UserTokenDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertToken(tokenEntity: TokenEntity)

    @Query("SELECT * FROM token")
    fun getToken(): TokenEntity?

    @Query("DELETE FROM token")
    fun clearTable()
}