package com.example.homeassistant.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.homeassistant.data.Property
import com.example.homeassistant.data.repository.PropertyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PropertyListViewModel(private val repository: PropertyRepository) : ViewModel() {

    private val _properties = MutableStateFlow<List<Property>>(emptyList())
    val properties: StateFlow<List<Property>> = _properties.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadProperties()
    }

    private fun loadProperties() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getAllProperties().collect { propertyList ->
                _properties.value = propertyList
                _isLoading.value = false
            }
        }
    }

    fun addProperty(property: Property) {
        viewModelScope.launch {
            repository.insertProperty(property)
            // The Flow will automatically update the UI
        }
    }

    fun updateProperty(property: Property) {
        viewModelScope.launch {
            repository.updateProperty(property)
            // The Flow will automatically update the UI
        }
    }

    fun deleteProperty(propertyId: String) {
        viewModelScope.launch {
            repository.deleteProperty(propertyId)
            // The Flow will automatically update the UI
        }
    }

    // ViewModel Factory
    class Factory(private val repository: PropertyRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PropertyListViewModel::class.java)) {
                return PropertyListViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}