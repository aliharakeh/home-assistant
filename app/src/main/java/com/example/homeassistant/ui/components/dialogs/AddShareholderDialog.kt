package com.example.homeassistant.ui.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.homeassistant.data.Currency
import com.example.homeassistant.data.ShareValue
import com.example.homeassistant.data.Shareholder

enum class ShareType {
    PERCENTAGE,
    CURRENCY
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddShareholderDialog(
    onDismiss: () -> Unit,
    onAdd: (Shareholder) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var shareType by remember { mutableStateOf(ShareType.PERCENTAGE) }
    var percentageValue by remember { mutableStateOf("") }
    var currencyAmount by remember { mutableStateOf("") }
    var selectedCurrency by remember { mutableStateOf(Currency.USD) }
    var currencyDropdownExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Shareholder") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Shareholder Name") },
                    placeholder = { Text("Enter name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Share Type",
                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier.selectable(
                            selected = (shareType == ShareType.PERCENTAGE),
                            onClick = { shareType = ShareType.PERCENTAGE }
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (shareType == ShareType.PERCENTAGE),
                            onClick = { shareType = ShareType.PERCENTAGE }
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Percentage")
                    }

                    Row(
                        modifier = Modifier.selectable(
                            selected = (shareType == ShareType.CURRENCY),
                            onClick = { shareType = ShareType.CURRENCY }
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (shareType == ShareType.CURRENCY),
                            onClick = { shareType = ShareType.CURRENCY }
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Currency")
                    }
                }

                when (shareType) {
                    ShareType.PERCENTAGE -> {
                        OutlinedTextField(
                            value = percentageValue,
                            onValueChange = { percentageValue = it },
                            label = { Text("Percentage (%)") },
                            placeholder = { Text("e.g., 50") },
                            modifier = Modifier.fillMaxWidth(),
                            suffix = { Text("%") }
                        )
                    }
                    ShareType.CURRENCY -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = currencyAmount,
                                onValueChange = { currencyAmount = it },
                                label = { Text("Amount") },
                                placeholder = { Text("e.g., 15000") },
                                modifier = Modifier.weight(1f)
                            )

                            ExposedDropdownMenuBox(
                                expanded = currencyDropdownExpanded,
                                onExpandedChange = { currencyDropdownExpanded = !currencyDropdownExpanded },
                                modifier = Modifier.weight(0.5f)
                            ) {
                                OutlinedTextField(
                                    value = selectedCurrency.name,
                                    onValueChange = { },
                                    readOnly = true,
                                    label = { Text("Currency") },
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(
                                            expanded = currencyDropdownExpanded
                                        )
                                    },
                                    modifier = Modifier.menuAnchor()
                                )

                                ExposedDropdownMenu(
                                    expanded = currencyDropdownExpanded,
                                    onDismissRequest = { currencyDropdownExpanded = false }
                                ) {
                                    Currency.values().forEach { currency ->
                                        DropdownMenuItem(
                                            text = { Text("${currency.name} (${currency.symbol})") },
                                            onClick = {
                                                selectedCurrency = currency
                                                currencyDropdownExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank()) {
                        val shareValue = when (shareType) {
                            ShareType.PERCENTAGE -> {
                                val percentage = percentageValue.toDoubleOrNull()
                                if (percentage != null && percentage > 0) {
                                    ShareValue.Percentage(percentage)
                                } else null
                            }
                            ShareType.CURRENCY -> {
                                val amount = currencyAmount.toDoubleOrNull()
                                if (amount != null && amount > 0) {
                                    ShareValue.CurrencyValue(amount, selectedCurrency)
                                } else null
                            }
                        }

                        shareValue?.let { value ->
                            onAdd(Shareholder(name, value))
                        }
                    }
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}