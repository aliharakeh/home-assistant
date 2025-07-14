package com.example.homeassistant.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.homeassistant.data.Currency
import com.example.homeassistant.data.Property
import com.example.homeassistant.data.RentDuration
import com.example.homeassistant.data.Subscription
import com.example.homeassistant.ui.components.dialogs.FilterBillsDialog

import com.example.homeassistant.ui.tabs.ElectricityBillsTab
import com.example.homeassistant.ui.tabs.InformationTab
import com.example.homeassistant.ui.tabs.SummaryTab
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PropertyEditScreen(
    property: Property? = null, // null for new property
    onNavigateBack: () -> Unit,
    onSaveProperty: (Property) -> Unit,
    onDeleteProperty: ((Property) -> Unit)? = null
) {
    var name by remember { mutableStateOf(property?.name ?: "") }
    var address by remember { mutableStateOf(property?.address ?: "") }
    var rentPrice by remember { mutableStateOf(property?.rentPrice?.toString() ?: "") }
    var rentDuration by remember { mutableStateOf(property?.rentDuration ?: RentDuration.MONTHLY) }
    var renterName by remember { mutableStateOf(property?.renterName ?: "") }
    var subscriptions by remember { mutableStateOf(property?.subscriptions ?: emptyList()) }
    var shareholders by remember { mutableStateOf(property?.shareholders ?: emptyList()) }
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var showGlobalAddBillDialog by remember { mutableStateOf(false) }

    // Filter states
    var filterFromDate by remember { mutableStateOf<LocalDate?>(null) }
    var filterToDate by remember { mutableStateOf<LocalDate?>(null) }
    var filterCurrency by remember { mutableStateOf<Currency?>(null) }
    var showFilterDialog by remember { mutableStateOf(false) }

    // Check if any filters are active
    val hasActiveFilters = filterFromDate != null || filterToDate != null || filterCurrency != null

    val isEditing = property != null
    val title = if (isEditing) "Edit Property" else "Add New Property"

    val tabs = listOf("Information", "Electricity Bills", "Summary")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            when (selectedTabIndex) {
                0 -> { // Information tab - Show save button
                    FloatingActionButton(
                        onClick = {
                            val newProperty = Property(
                                id = property?.id ?: System.currentTimeMillis().toString(),
                                name = name,
                                address = address,
                                rentPrice = rentPrice.toDoubleOrNull() ?: 0.0,
                                rentDuration = rentDuration,
                                renterName = if (renterName.isBlank()) null else renterName,
                                subscriptions = subscriptions,
                                shareholders = shareholders
                            )
                            onSaveProperty(newProperty)
                        },
                        containerColor = MaterialTheme.colorScheme.primary
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Save,
                            contentDescription = "Save Property",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
                1 -> { // Electricity Bills tab - Show add bill and filter buttons
                    if (subscriptions.isNotEmpty()) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Add bill button (left)
                            FloatingActionButton(
                                onClick = {
                                    showGlobalAddBillDialog = true
                                },
                                containerColor = MaterialTheme.colorScheme.primary
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    contentDescription = "Add Bill",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }

                            // Filter button (right)
                            FloatingActionButton(
                                onClick = {
                                    showFilterDialog = true
                                },
                                containerColor = if (hasActiveFilters)
                                    MaterialTheme.colorScheme.tertiary
                                else
                                    MaterialTheme.colorScheme.secondary
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.FilterList,
                                    contentDescription = if (hasActiveFilters) "Filter Bills (Active)" else "Filter Bills",
                                    tint = if (hasActiveFilters)
                                        MaterialTheme.colorScheme.onTertiary
                                    else
                                        MaterialTheme.colorScheme.onSecondary
                                )
                            }
                        }
                    }
                }
                2 -> { // Summary tab - Show filter button
                    if (subscriptions.isNotEmpty()) {
                        FloatingActionButton(
                            onClick = {
                                showFilterDialog = true
                            },
                            containerColor = if (hasActiveFilters)
                                MaterialTheme.colorScheme.tertiary
                            else
                                MaterialTheme.colorScheme.secondary
                        ) {
                            Icon(
                                imageVector = Icons.Filled.FilterList,
                                contentDescription = if (hasActiveFilters) "Filter Bills (Active)" else "Filter Bills",
                                tint = if (hasActiveFilters)
                                    MaterialTheme.colorScheme.onTertiary
                                else
                                    MaterialTheme.colorScheme.onSecondary
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Tab Row
            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.fillMaxWidth()
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) }
                    )
                }
            }

            // Filter indicator for Electricity Bills and Summary tabs
            if ((selectedTabIndex == 1 || selectedTabIndex == 2) && hasActiveFilters) {
                val filterText = buildString {
                    val filters = mutableListOf<String>()
                    filterFromDate?.let { filters.add("From: ${it.format(java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy"))}") }
                    filterToDate?.let { filters.add("To: ${it.format(java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy"))}") }
                    filterCurrency?.let { filters.add("Currency: ${it.symbol}") }
                    append("Active filters: ${filters.joinToString(", ")}")
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = filterText,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            }

            // Tab Content
            when (selectedTabIndex) {
                0 -> InformationTab(
                    name = name,
                    onNameChange = { name = it },
                    address = address,
                    onAddressChange = { address = it },
                    rentPrice = rentPrice,
                    onRentPriceChange = { rentPrice = it },
                    rentDuration = rentDuration,
                    onRentDurationChange = { rentDuration = it },
                    renterName = renterName,
                    onRenterNameChange = { renterName = it },
                    isEditing = isEditing,
                    property = property,
                    onDeleteProperty = onDeleteProperty,
                    subscriptions = subscriptions,
                    onSubscriptionsChange = { subscriptions = it },
                    shareholders = shareholders,
                    onShareholdersChange = { shareholders = it }
                )
                1 -> ElectricityBillsTab(
                    subscriptions = subscriptions,
                    onSubscriptionsChange = { subscriptions = it },
                    showGlobalAddBillDialog = showGlobalAddBillDialog,
                    onShowGlobalAddBillDialogChange = { showGlobalAddBillDialog = it },
                    filterFromDate = filterFromDate,
                    filterToDate = filterToDate,
                    filterCurrency = filterCurrency
                )
                2 -> SummaryTab(
                    subscriptions = subscriptions,
                    filterFromDate = filterFromDate,
                    filterToDate = filterToDate,
                    filterCurrency = filterCurrency
                )
            }
        }
    }

    // Filter Bills Dialog
    if (showFilterDialog) {
        FilterBillsDialog(
            initialFromDate = filterFromDate,
            initialToDate = filterToDate,
            initialCurrency = filterCurrency,
            onDismiss = { showFilterDialog = false },
            onApply = { fromDate, toDate, currency ->
                filterFromDate = fromDate
                filterToDate = toDate
                filterCurrency = currency
                showFilterDialog = false
            }
        )
    }
}