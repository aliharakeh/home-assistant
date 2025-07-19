package com.example.homeassistant.data.repository

import com.example.homeassistant.data.*
import com.example.homeassistant.data.database.PropertyDatabase
import com.example.homeassistant.data.database.mappers.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
class PropertyRepository(
    private val database: PropertyDatabase
) {
    private val propertyDao = database.propertyDao()
    private val subscriptionDao = database.subscriptionDao()
    private val electricityBillDao = database.electricityBillDao()
    private val shareholderDao = database.shareholderDao()

    // Property operations
    fun getAllProperties(): Flow<List<Property>> {
        return propertyDao.getAllProperties().map { entities ->
            entities.map { propertyEntity ->
                // Load complete property data including shareholders and subscriptions
                val subscriptions = getSubscriptionsForProperty(propertyEntity.id)
                val shareholders = getShareholdersForProperty(propertyEntity.id)
                propertyEntity.toProperty(subscriptions, shareholders)
            }
        }
    }

    suspend fun getCompletePropertyById(id: String): Property? {
        val propertyEntity = propertyDao.getPropertyById(id) ?: return null
        val subscriptions = getSubscriptionsForProperty(id)
        val shareholders = getShareholdersForProperty(id)
        return propertyEntity.toProperty(subscriptions, shareholders)
    }

    suspend fun getPropertyById(id: String): Property? {
        return getCompletePropertyById(id)
    }

    suspend fun insertProperty(property: Property) {
        // Insert the property
        propertyDao.insertProperty(property.toPropertyEntity())

        // Insert subscriptions and their bills
        property.subscriptions.forEach { subscription ->
            val subscriptionId = subscriptionDao.insertSubscription(
                subscription.toSubscriptionEntity(property.id)
            )

            subscription.electricityBills.forEach { bill ->
                electricityBillDao.insertBill(bill.toElectricityBillEntity(subscriptionId))
            }
        }

        // Insert shareholders
        property.shareholders.forEach { shareholder ->
            shareholderDao.insertShareholder(shareholder.toShareholderEntity(property.id))
        }
    }

    suspend fun updateProperty(property: Property) {
        // Update the property
        propertyDao.updateProperty(property.toPropertyEntity())

        // Delete existing related data and re-insert
        subscriptionDao.deleteSubscriptionsByPropertyId(property.id)
        shareholderDao.deleteShareholdersByPropertyId(property.id)

        // Re-insert subscriptions and their bills
        property.subscriptions.forEach { subscription ->
            val subscriptionId = subscriptionDao.insertSubscription(
                subscription.toSubscriptionEntity(property.id)
            )

            subscription.electricityBills.forEach { bill ->
                electricityBillDao.insertBill(bill.toElectricityBillEntity(subscriptionId))
            }
        }

        // Re-insert shareholders
        property.shareholders.forEach { shareholder ->
            shareholderDao.insertShareholder(shareholder.toShareholderEntity(property.id))
        }
    }

    suspend fun deleteProperty(propertyId: String) {
        propertyDao.deletePropertyById(propertyId)
        // Related data will be deleted automatically due to CASCADE
    }

    // Subscription operations
    suspend fun addSubscriptionToProperty(propertyId: String, subscription: Subscription) {
        val subscriptionId = subscriptionDao.insertSubscription(
            subscription.toSubscriptionEntity(propertyId)
        )

        subscription.electricityBills.forEach { bill ->
            electricityBillDao.insertBill(bill.toElectricityBillEntity(subscriptionId))
        }
    }

    // Electricity bill operations
    suspend fun addElectricityBill(subscriptionId: Long, bill: ElectricityBill) {
        electricityBillDao.insertBill(bill.toElectricityBillEntity(subscriptionId))
    }

    suspend fun updateElectricityBill(billId: Long, bill: ElectricityBill, subscriptionId: Long) {
        electricityBillDao.updateBill(
            bill.toElectricityBillEntity(subscriptionId).copy(id = billId)
        )
    }

    suspend fun deleteElectricityBill(billId: Long) {
        val bill = electricityBillDao.getBillById(billId)
        if (bill != null) {
            electricityBillDao.deleteBill(bill)
        }
    }

    // Shareholder operations
    suspend fun addShareholderToProperty(propertyId: String, shareholder: Shareholder) {
        shareholderDao.insertShareholder(shareholder.toShareholderEntity(propertyId))
    }

    suspend fun updateShareholder(shareholderId: Long, shareholder: Shareholder, propertyId: String) {
        shareholderDao.updateShareholder(
            shareholder.toShareholderEntity(propertyId).copy(id = shareholderId)
        )
    }

    suspend fun deleteShareholder(shareholderId: Long) {
        val shareholder = shareholderDao.getShareholderById(shareholderId)
        if (shareholder != null) {
            shareholderDao.deleteShareholder(shareholder)
        }
    }

    // Helper methods to load related data
    private suspend fun getSubscriptionsForProperty(propertyId: String): List<Subscription> {
        val subscriptionEntities = subscriptionDao.getSubscriptionsByPropertyId(propertyId).first()
        return subscriptionEntities.map { subscriptionEntity ->
            val billEntities = electricityBillDao.getBillsBySubscriptionId(subscriptionEntity.id).first()
            val bills = billEntities.map { it.toElectricityBill() }
            subscriptionEntity.toSubscription(bills)
        }
    }

    private suspend fun getShareholdersForProperty(propertyId: String): List<Shareholder> {
        val shareholderEntities = shareholderDao.getShareholdersByPropertyId(propertyId).first()
        return shareholderEntities.map { it.toShareholder() }
    }

    // Flow-based methods for UI to observe specific data
    fun getSubscriptionsForPropertyFlow(propertyId: String): Flow<List<Subscription>> {
        return subscriptionDao.getSubscriptionsByPropertyId(propertyId).map { subscriptionEntities ->
            subscriptionEntities.map { subscriptionEntity ->
                subscriptionEntity.toSubscription() // Bills would need to be loaded separately
            }
        }
    }

    fun getShareholdersForPropertyFlow(propertyId: String): Flow<List<Shareholder>> {
        return shareholderDao.getShareholdersByPropertyId(propertyId).map { shareholderEntities ->
            shareholderEntities.map { it.toShareholder() }
        }
    }

    // Get bills for a specific property
    fun getBillsByPropertyId(propertyId: String): Flow<List<ElectricityBill>> {
        return electricityBillDao.getBillsByPropertyId(propertyId).map { entities ->
            entities.map { it.toElectricityBill() }
        }
    }
}