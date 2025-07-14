package com.example.homeassistant.ui.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.homeassistant.data.Currency
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBillsDialog(
    initialFromDate: LocalDate?,
    initialToDate: LocalDate?,
    initialCurrency: Currency?,
    onDismiss: () -> Unit,
    onApply: (fromDate: LocalDate?, toDate: LocalDate?, currency: Currency?) -> Unit
) {
    var filterFromDate by remember { mutableStateOf(initialFromDate) }
    var filterToDate by remember { mutableStateOf(initialToDate) }
    var filterCurrency by remember { mutableStateOf(initialCurrency) }

    // Date picker states
    var showFromDatePicker by remember { mutableStateOf(false) }
    var showToDatePicker by remember { mutableStateOf(false) }
    var currencyExpanded by remember { mutableStateOf(false) }

    // Date picker states
    val fromDatePickerState = rememberDatePickerState(
        initialSelectedDateMillis = filterFromDate?.atStartOfDay(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
    )
    val toDatePickerState = rememberDatePickerState(
        initialSelectedDateMillis = filterToDate?.atStartOfDay(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Filter Bills") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // From Date
                OutlinedTextField(
                    value = filterFromDate?.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")) ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("From Date") },
                    placeholder = { Text("Select start date") },
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        Row {
                            if (filterFromDate != null) {
                                IconButton(onClick = { filterFromDate = null }) {
                                    Icon(
                                        imageVector = Icons.Filled.Delete,
                                        contentDescription = "Clear",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                            IconButton(onClick = { showFromDatePicker = true }) {
                                Icon(
                                    imageVector = Icons.Filled.Edit,
                                    contentDescription = "Pick Date"
                                )
                            }
                        }
                    }
                )

                // To Date
                OutlinedTextField(
                    value = filterToDate?.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")) ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("To Date") },
                    placeholder = { Text("Select end date") },
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        Row {
                            if (filterToDate != null) {
                                IconButton(onClick = { filterToDate = null }) {
                                    Icon(
                                        imageVector = Icons.Filled.Delete,
                                        contentDescription = "Clear",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                            IconButton(onClick = { showToDatePicker = true }) {
                                Icon(
                                    imageVector = Icons.Filled.Edit,
                                    contentDescription = "Pick Date"
                                )
                            }
                        }
                    }
                )

                // Currency Filter
                ExposedDropdownMenuBox(
                    expanded = currencyExpanded,
                    onExpandedChange = { currencyExpanded = !currencyExpanded }
                ) {
                    OutlinedTextField(
                        value = filterCurrency?.let { "${it.name} (${it.symbol})" } ?: "All Currencies",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Currency") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        trailingIcon = {
                            Row {
                                if (filterCurrency != null) {
                                    IconButton(onClick = { filterCurrency = null }) {
                                        Icon(
                                            imageVector = Icons.Filled.Delete,
                                            contentDescription = "Clear",
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = currencyExpanded)
                            }
                        }
                    )

                    ExposedDropdownMenu(
                        expanded = currencyExpanded,
                        onDismissRequest = { currencyExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("All Currencies") },
                            onClick = {
                                filterCurrency = null
                                currencyExpanded = false
                            }
                        )
                        Currency.values().forEach { currency ->
                            DropdownMenuItem(
                                text = { Text("${currency.name} (${currency.symbol})") },
                                onClick = {
                                    filterCurrency = currency
                                    currencyExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onApply(filterFromDate, filterToDate, filterCurrency)
                }
            ) {
                Text("Apply")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )

    // From Date Picker Dialog
    if (showFromDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showFromDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        fromDatePickerState.selectedDateMillis?.let { millis ->
                            filterFromDate = Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                        }
                        showFromDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showFromDatePicker = false }
                ) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = fromDatePickerState)
        }
    }

    // To Date Picker Dialog
    if (showToDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showToDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        toDatePickerState.selectedDateMillis?.let { millis ->
                            filterToDate = Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                        }
                        showToDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showToDatePicker = false }
                ) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = toDatePickerState)
        }
    }
}