package com.example.homeassistant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.homeassistant.ui.navigation.PropertyManagerApp
import com.example.homeassistant.ui.theme.HomeAssistantTheme
import com.example.homeassistant.ui.viewmodels.PropertyListViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HomeAssistantTheme {
                val application = application as PropertyManagerApplication
                val viewModel: PropertyListViewModel = viewModel(
                    factory = PropertyListViewModel.Factory(application.repository)
                )
                PropertyManagerApp(viewModel = viewModel)
            }
        }
    }
}