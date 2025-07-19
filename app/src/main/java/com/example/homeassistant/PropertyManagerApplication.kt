package com.example.homeassistant

import android.app.Application
import com.example.homeassistant.data.database.PropertyDatabase
import com.example.homeassistant.data.repository.PropertyRepository

class PropertyManagerApplication : Application() {

    val database by lazy { PropertyDatabase.getDatabase(this) }
    val repository by lazy { PropertyRepository(database) }
}