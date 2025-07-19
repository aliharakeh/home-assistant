package com.example.homeassistant.data.database.dao

import androidx.room.*
import com.example.homeassistant.data.database.entities.ElectricityBillEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ElectricityBillDao {
    @Query("SELECT * FROM electricity_bills WHERE subscriptionId = :subscriptionId ORDER BY paymentDate DESC")
    fun getBillsBySubscriptionId(subscriptionId: Long): Flow<List<ElectricityBillEntity>>

    @Query("SELECT * FROM electricity_bills WHERE id = :id")
    suspend fun getBillById(id: Long): ElectricityBillEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBill(bill: ElectricityBillEntity)

    @Update
    suspend fun updateBill(bill: ElectricityBillEntity)

    @Delete
    suspend fun deleteBill(bill: ElectricityBillEntity)

    @Query("DELETE FROM electricity_bills WHERE subscriptionId = :subscriptionId")
    suspend fun deleteBillsBySubscriptionId(subscriptionId: Long)

    @Query("SELECT * FROM electricity_bills eb INNER JOIN subscriptions s ON eb.subscriptionId = s.id WHERE s.propertyId = :propertyId ORDER BY eb.paymentDate DESC")
    fun getBillsByPropertyId(propertyId: String): Flow<List<ElectricityBillEntity>>
}