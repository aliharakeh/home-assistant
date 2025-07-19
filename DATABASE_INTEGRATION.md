# SQLite Database Integration

This document explains the SQLite database integration that has been added to your Property Manager Android app using Room.

## Overview

The app now uses Room (Android's SQLite object mapping library) to persist all property data, including:

-   Properties
-   Subscriptions
-   Electricity Bills
-   Shareholders

## Architecture

### Database Layer

-   **Room Database**: `PropertyDatabase` - Main database class
-   **Entities**: Database table representations
    -   `PropertyEntity`
    -   `SubscriptionEntity`
    -   `ElectricityBillEntity`
    -   `ShareholderEntity`
-   **DAOs**: Data Access Objects for database operations
    -   `PropertyDao`
    -   `SubscriptionDao`
    -   `ElectricityBillDao`
    -   `ShareholderDao`
-   **Type Converters**: Handle complex data types (LocalDate, enums)

### Repository Layer

-   **PropertyRepository**: Provides clean interface between database and UI
-   **Mappers**: Convert between database entities and domain models

### UI Layer

-   **PropertyListViewModel**: Manages UI state and coordinates with repository
-   **Updated MainActivity**: Initializes ViewModel with repository
-   **PropertyManagerApp**: Uses ViewModel instead of hardcoded data

## Key Files Added/Modified

### New Files:

```
app/src/main/java/com/example/homeassistant/data/database/
├── entities/DatabaseEntities.kt
├── dao/PropertyDao.kt
├── dao/SubscriptionDao.kt
├── dao/ElectricityBillDao.kt
├── dao/ShareholderDao.kt
├── converters/Converters.kt
├── mappers/EntityMappers.kt
└── PropertyDatabase.kt

app/src/main/java/com/example/homeassistant/data/repository/
└── PropertyRepository.kt

app/src/main/java/com/example/homeassistant/ui/viewmodels/
└── PropertyListViewModel.kt

app/src/main/java/com/example/homeassistant/ui/navigation/
└── PropertyManagerApp.kt

app/src/main/java/com/example/homeassistant/
└── PropertyManagerApplication.kt
```

### Modified Files:

-   `gradle/libs.versions.toml` - Added Room dependencies
-   `app/build.gradle.kts` - Added Room and KSP dependencies
-   `app/src/main/AndroidManifest.xml` - Registered custom Application class
-   `app/src/main/java/com/example/homeassistant/MainActivity.kt` - Integration with ViewModel

### Deleted Files:

-   Original `PropertyManagerApp.kt` - Replaced with database-integrated version
-   `DatabasePopulator.kt` - Removed dummy data functionality

## Dependencies Added

```toml
# gradle/libs.versions.toml
room = "2.6.1"
ksp = "2.0.21-1.0.25"

androidx-room-runtime = { group = "androidx.room", name = "room-runtime", version.ref = "room" }
androidx-room-compiler = { group = "androidx.room", name = "room-compiler", version.ref = "room" }
androidx-room-ktx = { group = "androidx.room", name = "room-ktx", version.ref = "room" }
ksp = { id = "com.google.devtools.ksp", version.ref = "ksp" }
```

## Database Schema

### Properties Table

-   `id` (String, Primary Key)
-   `name` (String)
-   `address` (String)
-   `electricityCodeNumber` (String, nullable)
-   `rentPrice` (Double)
-   `rentDuration` (String)
-   `renterName` (String, nullable)

### Subscriptions Table

-   `id` (Long, Auto-generated Primary Key)
-   `name` (String)
-   `propertyId` (String, Foreign Key)

### Electricity Bills Table

-   `id` (Long, Auto-generated Primary Key)
-   `amount` (Double)
-   `currency` (String)
-   `paymentDate` (String, ISO format)
-   `subscriptionId` (Long, Foreign Key)

### Shareholders Table

-   `id` (Long, Auto-generated Primary Key)
-   `name` (String)
-   `shareValueType` (String: "percentage" or "currency")
-   `shareValue` (Double)
-   `currency` (String, nullable)
-   `propertyId` (String, Foreign Key)

## Usage Examples

### Basic Repository Operations

```kotlin
// Get all properties (Flow)
repository.getAllProperties().collect { properties ->
    // Update UI
}

// Add new property
val newProperty = Property(...)
repository.insertProperty(newProperty)

// Update existing property
repository.updateProperty(updatedProperty)

// Delete property
repository.deleteProperty(propertyId)
```

### Using in ViewModel

```kotlin
class PropertyListViewModel(private val repository: PropertyRepository) : ViewModel() {
    val properties = repository.getAllProperties()

    fun addProperty(property: Property) {
        viewModelScope.launch {
            repository.insertProperty(property)
        }
    }
}
```

## Initial State

The app starts with an empty database. Users can add their own properties through the UI.

## Migration from Hardcoded Data

To complete the migration:

1. Replace the old `PropertyManagerApp()` call in `MainActivity` with the new database-integrated version
2. Update any other screens that directly use hardcoded property data
3. Consider implementing proper Flow combining in the repository for better performance

## Notes

-   The current implementation uses simplified Flow handling in some areas for clarity
-   Foreign key constraints ensure data integrity with CASCADE deletion
-   Type converters handle LocalDate and enum serialization automatically
-   The repository provides both suspend functions and Flow-based reactive data access

## Future Improvements

1. Implement proper Flow combining for loading complete Property objects with relationships
2. Add database migration strategies for schema changes
3. Implement data synchronization if backend integration is needed
4. Add database backup/restore functionality
5. Consider implementing a more sophisticated caching strategy
