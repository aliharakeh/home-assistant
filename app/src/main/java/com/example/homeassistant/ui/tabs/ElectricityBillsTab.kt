package com.example.homeassistant.ui.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.homeassistant.data.Currency
import com.example.homeassistant.data.ElectricityBill
import com.example.homeassistant.data.Subscription
import com.example.homeassistant.ui.components.dialogs.ConfirmationDialog
import com.example.homeassistant.ui.components.dialogs.GlobalAddBillDialog
import com.example.homeassistant.ui.components.dialogs.EditBillDialog
import com.example.homeassistant.ui.components.items.BillItem
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ElectricityBillsTab(
    subscriptions: List<Subscription>,
    onSubscriptionsChange: (List<Subscription>) -> Unit,
    showGlobalAddBillDialog: Boolean,
    onShowGlobalAddBillDialogChange: (Boolean) -> Unit,
    filterFromDate: LocalDate?,
    filterToDate: LocalDate?,
    filterCurrency: Currency?
) {

    // Dialog states
    var showEditBillDialog by remember { mutableStateOf(false) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var showDeleteAllConfirmation by remember { mutableStateOf(false) }
    var selectedSubscriptionIndex by remember { mutableIntStateOf(0) }
    var selectedBillIndex by remember { mutableIntStateOf(-1) }
    var deleteAction by remember { mutableStateOf<(() -> Unit)?>(null) }

    // Tab state for subscriptions
    var selectedSubscriptionTab by remember { mutableIntStateOf(0) }

    // Filter bills based on current filters
    val filteredSubscriptions by remember(subscriptions, filterFromDate, filterToDate, filterCurrency) {
        derivedStateOf {
            subscriptions.map { subscription ->
                val filteredBills = subscription.electricityBills.filter { bill ->
                    val dateMatch = (filterFromDate == null || !bill.paymentDate.isBefore(filterFromDate)) &&
                            (filterToDate == null || !bill.paymentDate.isAfter(filterToDate))
                    val currencyMatch = filterCurrency == null || bill.currency == filterCurrency
                    dateMatch && currencyMatch
                }
                subscription.copy(electricityBills = filteredBills)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Electricity Bills",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            // Delete All Bills button
            Button(
                onClick = { showDeleteAllConfirmation = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                enabled = subscriptions.any { it.electricityBills.isNotEmpty() }
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete All Bills",
                    modifier = Modifier.padding(end = 4.dp)
                )
                Text("Delete All")
            }
        }

        if (subscriptions.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No subscriptions available",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Add subscriptions in the Information tab first",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            // Subscription tabs
            TabRow(
                selectedTabIndex = selectedSubscriptionTab.coerceAtMost(subscriptions.size - 1)
            ) {
                subscriptions.forEachIndexed { index, subscription ->
                                        Tab(
                        selected = selectedSubscriptionTab == index,
                        onClick = { selectedSubscriptionTab = index },
                        text = {
                            Text(subscription.name)
                        }
                    )
                }
            }

            // Adjust selectedSubscriptionTab if it's out of bounds
            if (selectedSubscriptionTab >= subscriptions.size) {
                selectedSubscriptionTab = (subscriptions.size - 1).coerceAtLeast(0)
            }

            // Current subscription content
            if (subscriptions.isNotEmpty() && selectedSubscriptionTab < subscriptions.size) {
                val currentSubscription = filteredSubscriptions[selectedSubscriptionTab]

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (currentSubscription.electricityBills.isEmpty()) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = if (subscriptions[selectedSubscriptionTab].electricityBills.isEmpty())
                                        "No bills added yet"
                                    else "No bills match the current filter",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    } else {
                        currentSubscription.electricityBills.forEachIndexed { billIndex, bill ->
                            // Find the original bill index in the unfiltered subscription
                            val originalBillIndex = subscriptions[selectedSubscriptionTab].electricityBills.indexOf(bill)
                            BillItem(
                                bill = bill,
                                onEdit = {
                                    selectedSubscriptionIndex = selectedSubscriptionTab
                                    selectedBillIndex = originalBillIndex
                                    showEditBillDialog = true
                                },
                                onDelete = {
                                    selectedSubscriptionIndex = selectedSubscriptionTab
                                    selectedBillIndex = originalBillIndex
                                    deleteAction = {
                                        val subscription = subscriptions[selectedSubscriptionIndex]
                                        val updatedBills = subscription.electricityBills.toMutableList()
                                        updatedBills.removeAt(selectedBillIndex)
                                        val updatedSubscription = subscription.copy(electricityBills = updatedBills)
                                        onSubscriptionsChange(subscriptions.toMutableList().apply {
                                            set(selectedSubscriptionIndex, updatedSubscription)
                                        })
                                    }
                                    showDeleteConfirmation = true
                                }
                            )
                        }
                    }

                }
            }
        }
    }

    // Global Add Bill Dialog
    if (showGlobalAddBillDialog) {
        GlobalAddBillDialog(
            subscriptions = subscriptions,
            onDismiss = { onShowGlobalAddBillDialogChange(false) },
            onAdd = { subscriptionIndex, bill ->
                val subscription = subscriptions[subscriptionIndex]
                val updatedBills = subscription.electricityBills + bill
                val updatedSubscription = subscription.copy(electricityBills = updatedBills)
                onSubscriptionsChange(subscriptions.toMutableList().apply {
                    set(subscriptionIndex, updatedSubscription)
                })
                onShowGlobalAddBillDialogChange(false)
            }
        )
    }

    // Edit Bill Dialog
    if (showEditBillDialog && selectedSubscriptionIndex >= 0 && selectedBillIndex >= 0) {
        val currentBill = subscriptions[selectedSubscriptionIndex].electricityBills[selectedBillIndex]
        EditBillDialog(
            bill = currentBill,
            onDismiss = {
                showEditBillDialog = false
                selectedSubscriptionIndex = -1
                selectedBillIndex = -1
            },
            onSave = { updatedBill ->
                val subscription = subscriptions[selectedSubscriptionIndex]
                val updatedBills = subscription.electricityBills.toMutableList()
                updatedBills[selectedBillIndex] = updatedBill
                val updatedSubscription = subscription.copy(electricityBills = updatedBills)
                onSubscriptionsChange(subscriptions.toMutableList().apply {
                    set(selectedSubscriptionIndex, updatedSubscription)
                })
                showEditBillDialog = false
                selectedSubscriptionIndex = -1
                selectedBillIndex = -1
            }
        )
    }

    // Delete Bill Confirmation Dialog
    if (showDeleteConfirmation) {
        ConfirmationDialog(
            title = "Delete Bill",
            message = "Are you sure you want to delete this bill?",
            onConfirm = {
                deleteAction?.invoke()
                showDeleteConfirmation = false
                deleteAction = null
            },
            onDismiss = {
                showDeleteConfirmation = false
                deleteAction = null
            }
        )
    }

    // Delete All Bills Confirmation Dialog
    if (showDeleteAllConfirmation) {
        ConfirmationDialog(
            title = "Delete All Bills",
            message = "Are you sure you want to delete all bills matching the current filter? This action cannot be undone.",
            filterFromDate = filterFromDate,
            filterToDate = filterToDate,
            onConfirm = {
                val updatedSubscriptions = subscriptions.map { subscription ->
                    val filteredBills = subscription.electricityBills.filterNot { bill ->
                        val dateMatch = (filterFromDate == null || !bill.paymentDate.isBefore(filterFromDate)) &&
                                (filterToDate == null || !bill.paymentDate.isAfter(filterToDate))
                        val currencyMatch = filterCurrency == null || bill.currency == filterCurrency
                        dateMatch && currencyMatch
                    }
                    subscription.copy(electricityBills = filteredBills)
                }
                onSubscriptionsChange(updatedSubscriptions)
                showDeleteAllConfirmation = false
            },
            onDismiss = {
                showDeleteAllConfirmation = false
            }
        )
    }

}