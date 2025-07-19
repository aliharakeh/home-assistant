package com.example.homeassistant.data.database.mappers

import com.example.homeassistant.data.*
import com.example.homeassistant.data.database.entities.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// Property mappings
fun PropertyEntity.toProperty(
    subscriptions: List<Subscription> = emptyList(),
    shareholders: List<Shareholder> = emptyList()
): Property {
    return Property(
        id = id,
        name = name,
        address = address,
        electricityCodeNumber = electricityCodeNumber,
        rentPrice = rentPrice,
        rentDuration = RentDuration.valueOf(rentDuration),
        renterName = renterName,
        subscriptions = subscriptions,
        shareholders = shareholders
    )
}

fun Property.toPropertyEntity(): PropertyEntity {
    return PropertyEntity(
        id = id,
        name = name,
        address = address,
        electricityCodeNumber = electricityCodeNumber,
        rentPrice = rentPrice,
        rentDuration = rentDuration.name,
        renterName = renterName
    )
}

// Subscription mappings
fun SubscriptionEntity.toSubscription(electricityBills: List<ElectricityBill> = emptyList()): Subscription {
    return Subscription(
        name = name,
        electricityBills = electricityBills
    )
}

fun Subscription.toSubscriptionEntity(propertyId: String): SubscriptionEntity {
    return SubscriptionEntity(
        name = name,
        propertyId = propertyId
    )
}

// ElectricityBill mappings
fun ElectricityBillEntity.toElectricityBill(): ElectricityBill {
    return ElectricityBill(
        amount = amount,
        currency = Currency.valueOf(currency),
        paymentDate = LocalDate.parse(paymentDate, DateTimeFormatter.ISO_LOCAL_DATE)
    )
}

fun ElectricityBill.toElectricityBillEntity(subscriptionId: Long): ElectricityBillEntity {
    return ElectricityBillEntity(
        amount = amount,
        currency = currency.name,
        paymentDate = paymentDate.format(DateTimeFormatter.ISO_LOCAL_DATE),
        subscriptionId = subscriptionId
    )
}

// Shareholder mappings
fun ShareholderEntity.toShareholder(): Shareholder {
    val shareValue = when (shareValueType) {
        "percentage" -> ShareValue.Percentage(shareValue)
        "currency" -> ShareValue.CurrencyValue(shareValue, Currency.valueOf(currency!!))
        else -> throw IllegalStateException("Unknown share value type: $shareValueType")
    }

    return Shareholder(
        name = name,
        shareValue = shareValue
    )
}

fun Shareholder.toShareholderEntity(propertyId: String): ShareholderEntity {
    return when (shareValue) {
        is ShareValue.Percentage -> ShareholderEntity(
            name = name,
            shareValueType = "percentage",
            shareValue = shareValue.value,
            currency = null,
            propertyId = propertyId
        )
        is ShareValue.CurrencyValue -> ShareholderEntity(
            name = name,
            shareValueType = "currency",
            shareValue = shareValue.amount,
            currency = shareValue.currency.name,
            propertyId = propertyId
        )
    }
}