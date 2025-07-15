package com.example.homeassistant.ui.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.homeassistant.data.Property
import com.example.homeassistant.data.RentDuration
import com.example.homeassistant.data.Shareholder
import com.example.homeassistant.data.Subscription
import com.example.homeassistant.ui.components.dialogs.AddShareholderDialog
import com.example.homeassistant.ui.components.dialogs.AddSubscriptionDialog
import com.example.homeassistant.ui.components.dialogs.ConfirmationDialog
import com.example.homeassistant.ui.components.dialogs.EditShareholderDialog

@Composable
fun InformationTab(
    name: String,
    onNameChange: (String) -> Unit,
    address: String,
    onAddressChange: (String) -> Unit,
    rentPrice: String,
    onRentPriceChange: (String) -> Unit,
    rentDuration: RentDuration,
    onRentDurationChange: (RentDuration) -> Unit,
    renterName: String,
    onRenterNameChange: (String) -> Unit,
    isEditing: Boolean,
    property: Property?,
    onDeleteProperty: ((Property) -> Unit)?,
    subscriptions: List<Subscription>,
    onSubscriptionsChange: (List<Subscription>) -> Unit,
    shareholders: List<Shareholder>,
    onShareholdersChange: (List<Shareholder>) -> Unit
) {
    var showAddSubscriptionDialog by remember { mutableStateOf(false) }
    var showDeleteSubscriptionConfirmation by remember { mutableStateOf(false) }
    var subscriptionToDelete by remember { mutableStateOf(-1) }

    // Shareholder dialog states
    var showAddShareholderDialog by remember { mutableStateOf(false) }
    var showEditShareholderDialog by remember { mutableStateOf(false) }
    var showDeleteShareholderConfirmation by remember { mutableStateOf(false) }
    var shareholderToEdit by remember { mutableStateOf<Shareholder?>(null) }
    var shareholderToDelete by remember { mutableStateOf(-1) }

    // Property deletion confirmation state
    var showDeletePropertyConfirmation by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Basic Information Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Basic Information",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = onNameChange,
                    label = { Text("Property Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = address,
                    onValueChange = onAddressChange,
                    label = { Text("Address") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2
                )
            }
        }

        // Rent Information Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Rent Information",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                OutlinedTextField(
                    value = rentPrice,
                    onValueChange = onRentPriceChange,
                    label = { Text("Rent Price") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Text(
                    text = "Rent Duration",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    RentDuration.values().forEach { duration ->
                        Row(
                            modifier = Modifier.selectable(
                                selected = (rentDuration == duration),
                                onClick = { onRentDurationChange(duration) }
                            ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (rentDuration == duration),
                                onClick = { onRentDurationChange(duration) }
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = duration.name.lowercase().replaceFirstChar { it.uppercase() },
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }

        // Renter Information Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Renter Information",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                OutlinedTextField(
                    value = renterName,
                    onValueChange = onRenterNameChange,
                    label = { Text("Renter Name (Optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        }

        // Shareholders Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Shareholders",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Button(
                        onClick = { showAddShareholderDialog = true }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Add Shareholder",
                            modifier = Modifier.padding(end = 4.dp)
                        )
                        Text("Add")
                    }
                }

                if (shareholders.isEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "No shareholders added yet",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } else {
                    shareholders.forEachIndexed { index, shareholder ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = shareholder.name,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )

                                    Text(
                                        text = shareholder.shareValue.toString(),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Medium
                                    )
                                }

                                Row {
                                    IconButton(
                                        onClick = {
                                            shareholderToEdit = shareholder
                                            showEditShareholderDialog = true
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Edit,
                                            contentDescription = "Edit Shareholder",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }

                                    IconButton(
                                        onClick = {
                                            shareholderToDelete = index
                                            showDeleteShareholderConfirmation = true
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Delete,
                                            contentDescription = "Delete Shareholder",
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Electricity Subscriptions Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Electricity Subscriptions",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Button(
                        onClick = { showAddSubscriptionDialog = true }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Add Subscription",
                            modifier = Modifier.padding(end = 4.dp)
                        )
                        Text("Add")
                    }
                }

                if (subscriptions.isEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "No subscriptions added yet",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } else {
                    subscriptions.forEachIndexed { index, subscription ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = subscription.name,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )

                                    Text(
                                        text = "${subscription.electricityBills.size} bills",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        subscriptionToDelete = index
                                        showDeleteSubscriptionConfirmation = true
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Delete,
                                        contentDescription = "Delete Subscription",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Delete Button (only show when editing existing property)
        if (isEditing && property != null && onDeleteProperty != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Danger Zone",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )

                    Text(
                        text = "Once you delete this property, there is no going back. Please be certain.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )

                    Button(
                        onClick = { showDeletePropertyConfirmation = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete",
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text("Delete Property")
                    }
                }
            }
        }

        // Add some bottom spacing for the FAB
        Spacer(modifier = Modifier.height(80.dp))
    }

    // Add Subscription Dialog
    if (showAddSubscriptionDialog) {
        AddSubscriptionDialog(
            onDismiss = { showAddSubscriptionDialog = false },
            onAdd = { subscription ->
                onSubscriptionsChange(subscriptions + subscription)
                showAddSubscriptionDialog = false
            }
        )
    }

    // Delete Subscription Confirmation Dialog
    if (showDeleteSubscriptionConfirmation && subscriptionToDelete >= 0) {
        ConfirmationDialog(
            title = "Delete Subscription",
            message = "Are you sure you want to delete this subscription? This will remove all associated bills (${subscriptions[subscriptionToDelete].electricityBills.size} bills).",
            onConfirm = {
                onSubscriptionsChange(subscriptions.toMutableList().apply {
                    removeAt(subscriptionToDelete)
                })
                showDeleteSubscriptionConfirmation = false
                subscriptionToDelete = -1
            },
            onDismiss = {
                showDeleteSubscriptionConfirmation = false
                subscriptionToDelete = -1
            }
        )
    }

    // Add Shareholder Dialog
    if (showAddShareholderDialog) {
        AddShareholderDialog(
            onDismiss = { showAddShareholderDialog = false },
            onAdd = { shareholder ->
                onShareholdersChange(shareholders + shareholder)
                showAddShareholderDialog = false
            }
        )
    }

    // Edit Shareholder Dialog
    if (showEditShareholderDialog && shareholderToEdit != null) {
        EditShareholderDialog(
            shareholder = shareholderToEdit!!,
            onDismiss = {
                showEditShareholderDialog = false
                shareholderToEdit = null
            },
            onSave = { updatedShareholder ->
                val index = shareholders.indexOf(shareholderToEdit)
                if (index >= 0) {
                    onShareholdersChange(shareholders.toMutableList().apply {
                        set(index, updatedShareholder)
                    })
                }
                showEditShareholderDialog = false
                shareholderToEdit = null
            }
        )
    }

    // Delete Shareholder Confirmation Dialog
    if (showDeleteShareholderConfirmation && shareholderToDelete >= 0) {
        ConfirmationDialog(
            title = "Delete Shareholder",
            message = "Are you sure you want to delete ${shareholders[shareholderToDelete].name} from the shareholders list?",
            onConfirm = {
                onShareholdersChange(shareholders.toMutableList().apply {
                    removeAt(shareholderToDelete)
                })
                showDeleteShareholderConfirmation = false
                shareholderToDelete = -1
            },
            onDismiss = {
                showDeleteShareholderConfirmation = false
                shareholderToDelete = -1
            }
        )
    }

    // Delete Property Confirmation Dialog
    if (showDeletePropertyConfirmation && property != null && onDeleteProperty != null) {
        ConfirmationDialog(
            title = "Delete Property",
            message = "Are you sure you want to delete \"${property.name}\"? This action cannot be undone and will permanently remove the property along with all its subscriptions, bills, and shareholder information.",
            onConfirm = {
                onDeleteProperty(property)
                showDeletePropertyConfirmation = false
            },
            onDismiss = {
                showDeletePropertyConfirmation = false
            }
        )
    }
}