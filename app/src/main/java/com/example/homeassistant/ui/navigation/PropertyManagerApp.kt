package com.example.homeassistant.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.homeassistant.data.Property
import com.example.homeassistant.ui.screens.PropertyEditScreen
import com.example.homeassistant.ui.screens.PropertyListScreen
import com.example.homeassistant.ui.viewmodels.PropertyListViewModel
import java.util.UUID

@Composable
fun PropertyManagerApp(viewModel: PropertyListViewModel) {
    val navController = rememberNavController()

    // Collect properties from the ViewModel
    val properties by viewModel.properties.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Manage the currently selected property for editing
    var currentProperty by remember { mutableStateOf<Property?>(null) }

    NavHost(
        navController = navController,
        startDestination = "property_list"
    ) {
        composable("property_list") {
            PropertyListScreen(
                properties = properties,
                onPropertyClick = { property ->
                    currentProperty = property
                    navController.navigate("property_edit")
                },
                onAddProperty = {
                    currentProperty = null
                    navController.navigate("property_edit")
                }
            )
        }

        composable("property_edit") {
            PropertyEditScreen(
                property = currentProperty,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onSaveProperty = { property ->
                    if (currentProperty == null) {
                        // Adding new property - generate a unique ID
                        val newProperty = property.copy(id = UUID.randomUUID().toString())
                        viewModel.addProperty(newProperty)
                    } else {
                        // Updating existing property
                        viewModel.updateProperty(property)
                    }
                    navController.popBackStack()
                },
                onDeleteProperty = if (currentProperty != null) {
                    { property ->
                        viewModel.deleteProperty(property.id)
                        navController.popBackStack()
                    }
                } else null
            )
        }
    }
}