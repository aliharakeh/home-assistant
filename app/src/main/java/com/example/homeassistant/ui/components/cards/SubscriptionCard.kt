package com.example.homeassistant.ui.components.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ElectricalServices
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
import com.example.homeassistant.ui.theme.getSemanticColors

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
    val semanticColors = getSemanticColors()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header with subscription name and delete button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.ElectricalServices,
                        contentDescription = "Subscription",
                        modifier = Modifier.size(20.dp),
                        tint = semanticColors.warningOrange
                    )
                    Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                    Text(
                        text = subscription.name,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                IconButton(onClick = { showDeleteSubscriptionConfirmation = true }) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete Subscription",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Bills content
            if (subscription.electricityBills.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "No bills added yet",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
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