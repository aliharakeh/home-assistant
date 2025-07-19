package com.example.homeassistant.data.database.dao

import androidx.room.*
import com.example.homeassistant.data.database.entities.ShareholderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShareholderDao {
    @Query("SELECT * FROM shareholders WHERE propertyId = :propertyId")
    fun getShareholdersByPropertyId(propertyId: String): Flow<List<ShareholderEntity>>

    @Query("SELECT * FROM shareholders WHERE id = :id")
    suspend fun getShareholderById(id: Long): ShareholderEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShareholder(shareholder: ShareholderEntity)

    @Update
    suspend fun updateShareholder(shareholder: ShareholderEntity)

    @Delete
    suspend fun deleteShareholder(shareholder: ShareholderEntity)

    @Query("DELETE FROM shareholders WHERE propertyId = :propertyId")
    suspend fun deleteShareholdersByPropertyId(propertyId: String)
}