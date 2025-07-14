package com.example.homeassistant.ui.components.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.example.homeassistant.data.Subscription
import com.example.homeassistant.ui.components.dialogs.ConfirmationDialog
import com.example.homeassistant.ui.components.items.BillItem

@Composable
fun SubscriptionCard(
    subscription: Subscription,
    onEditBill: (Int) -> Unit,
    onDeleteBill: (Int) -> Unit,
    onDeleteSubscription: () -> Unit
) {
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var showDeleteSubscriptionConfirmation by remember { mutableStateOf(false) }
    var deleteBillIndex by remember { mutableStateOf(-1) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = subscription.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )

                IconButton(onClick = { showDeleteSubscriptionConfirmation = true }) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete Subscription",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            if (subscription.electricityBills.isEmpty()) {
                Text(
                    text = "No bills added yet",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                subscription.electricityBills.forEachIndexed { index, bill ->
                    BillItem(
                        bill = bill,
                        onEdit = { onEditBill(index) },
                        onDelete = {
                            deleteBillIndex = index
                            showDeleteConfirmation = true
                        }
                    )
                }
            }
        }
    }

    // Delete Bill Confirmation Dialog
    if (showDeleteConfirmation && deleteBillIndex >= 0) {
        ConfirmationDialog(
            title = "Delete Bill",
            message = "Are you sure you want to delete this bill?",
            onConfirm = {
                onDeleteBill(deleteBillIndex)
                showDeleteConfirmation = false
                deleteBillIndex = -1
            },
            onDismiss = {
                showDeleteConfirmation = false
                deleteBillIndex = -1
            }
        )
    }

    // Delete Subscription Confirmation Dialog
    if (showDeleteSubscriptionConfirmation) {
        ConfirmationDialog(
            title = "Delete Subscription",
            message = "Are you sure you want to delete this subscription? This will remove all associated bills.",
            onConfirm = {
                onDeleteSubscription()
                showDeleteSubscriptionConfirmation = false
            },
            onDismiss = {
                showDeleteSubscriptionConfirmation = false
            }
        )
    }
}