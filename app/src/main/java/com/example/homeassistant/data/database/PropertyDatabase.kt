package com.example.homeassistant.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.example.homeassistant.data.database.entities.PropertyEntity
import com.example.homeassistant.data.database.entities.SubscriptionEntity
import com.example.homeassistant.data.database.entities.ElectricityBillEntity
import com.example.homeassistant.data.database.entities.ShareholderEntity
import com.example.homeassistant.data.database.dao.PropertyDao
import com.example.homeassistant.data.database.dao.SubscriptionDao
import com.example.homeassistant.data.database.dao.ElectricityBillDao
import com.example.homeassistant.data.database.dao.ShareholderDao
import com.example.homeassistant.data.database.converters.Converters

@Database(
    entities = [
        PropertyEntity::class,
        SubscriptionEntity::class,
        ElectricityBillEntity::class,
        ShareholderEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class PropertyDatabase : RoomDatabase() {
    abstract fun propertyDao(): PropertyDao
    abstract fun subscriptionDao(): SubscriptionDao
    abstract fun electricityBillDao(): ElectricityBillDao
    abstract fun shareholderDao(): ShareholderDao

    companion object {
        @Volatile
        private var INSTANCE: PropertyDatabase? = null

        fun getDatabase(context: Context): PropertyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PropertyDatabase::class.java,
                    "property_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}