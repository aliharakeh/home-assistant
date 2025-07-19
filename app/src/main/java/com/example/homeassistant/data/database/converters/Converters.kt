package com.example.homeassistant.data.database.converters

import androidx.room.TypeConverter
import com.example.homeassistant.data.Currency
import com.example.homeassistant.data.RentDuration
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Converters {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? {
        return date?.format(formatter)
    }

    @TypeConverter
    fun toLocalDate(dateString: String?): LocalDate? {
        return dateString?.let { LocalDate.parse(it, formatter) }
    }

    @TypeConverter
    fun fromCurrency(currency: Currency?): String? {
        return currency?.name
    }

    @TypeConverter
    fun toCurrency(currencyString: String?): Currency? {
        return currencyString?.let { Currency.valueOf(it) }
    }

    @TypeConverter
    fun fromRentDuration(rentDuration: RentDuration?): String? {
        return rentDuration?.name
    }

    @TypeConverter
    fun toRentDuration(rentDurationString: String?): RentDuration? {
        return rentDurationString?.let { RentDuration.valueOf(it) }
    }
}