package com.example.homeassistant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.homeassistant.ui.navigation.PropertyManagerApp
import com.example.homeassistant.ui.theme.HomeAssistantTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HomeAssistantTheme {
                PropertyManagerApp()
            }
        }
    }
}