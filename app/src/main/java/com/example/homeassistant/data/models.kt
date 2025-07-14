package com.example.homeassistant.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ElectricalServices
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Group
import androidx.compose.ui.graphics.vector.ImageVector
import java.time.LocalDate

// Sealed class to represent different types of share values
sealed class ShareValue {
    data class Percentage(val value: Double) : ShareValue() {
        override fun toString(): String = "$value%"
    }

    data class CurrencyValue(val amount: Double, val currency: Currency) : ShareValue() {
        override fun toString(): String = "${currency.symbol}$amount"
    }
}

data class Shareholder(
    val name: String,
    val shareValue: ShareValue
)

data class Property(
    val id: String,
    val name: String,
    val address: String,
    val rentPrice: Double,
    val rentDuration: RentDuration,
    val renterName: String? = null, // Nullable if no renter
    val subscriptions: List<Subscription> = emptyList(),
    val shareholders: List<Shareholder> = emptyList()
)

data class Subscription(
    val name: String, // "motor" or "main"
    val electricityBills: List<ElectricityBill> = emptyList()
)

data class ElectricityBill(
    val amount: Double,
    val currency: Currency,
    val paymentDate: LocalDate
)

enum class Currency(val symbol: String) {
    USD("$"),
    LBP("L.L.");

    override fun toString(): String = symbol
}

enum class RentDuration {
    MONTHLY,
    YEARLY
}

// Helper function to get icons (optional, but good for organization)
fun getIconForPropertyDetail(type: PropertyDetailType): ImageVector {
    return when (type) {
        PropertyDetailType.NAME -> Icons.Filled.Home
        PropertyDetailType.ADDRESS -> Icons.Filled.LocationOn
        PropertyDetailType.RENT_PRICE -> Icons.Filled.AttachMoney
        PropertyDetailType.RENT_DURATION -> Icons.Filled.CalendarToday
        PropertyDetailType.RENTER_NAME -> Icons.Filled.Person
        PropertyDetailType.SUBSCRIPTIONS -> Icons.Filled.ElectricalServices
        PropertyDetailType.SHAREHOLDERS -> Icons.Filled.Group
    }
}

enum class PropertyDetailType {
    NAME,
    ADDRESS,
    RENT_PRICE,
    RENT_DURATION,
    RENTER_NAME,
    SUBSCRIPTIONS,
    SHAREHOLDERS
}