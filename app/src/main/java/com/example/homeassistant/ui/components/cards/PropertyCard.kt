package com.example.homeassistant.ui.components.cards

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ElectricalServices
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.homeassistant.data.Property
import com.example.homeassistant.ui.components.items.PropertyDetailRow

@Composable
fun PropertyCard(
    property: Property,
    modifier: Modifier = Modifier,
    onClick: (Property) -> Unit = {}
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick(property) },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header with property name on left and expand button on right
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left side - Property name
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Home,
                        contentDescription = "Property",
                        modifier = Modifier.size(20.dp),
                        tint = androidx.compose.ui.graphics.Color(0xFF2E7D32) // Green for home
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = property.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                // Right side - Expand button only
                IconButton(
                    onClick = { isExpanded = !isExpanded }
                ) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                        contentDescription = if (isExpanded) "Show less" else "Show more",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Address and rent info row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                // Left side - Address
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = "Address",
                        modifier = Modifier.size(20.dp),
                        tint = androidx.compose.ui.graphics.Color(0xFFD32F2F) // Red for location
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = property.address,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Right side - Rent price and duration on same line
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.AttachMoney,
                        contentDescription = "Rent",
                        modifier = Modifier.size(16.dp),
                        tint = androidx.compose.ui.graphics.Color(0xFF388E3C) // Green for money
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${property.rentPrice}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Filled.CalendarToday,
                        contentDescription = "Duration",
                        modifier = Modifier.size(14.dp),
                        tint = androidx.compose.ui.graphics.Color(0xFF1976D2) // Blue for calendar
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = property.rentDuration.name.lowercase().replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Expanded details with animation
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))

                    // Divider
                    androidx.compose.material3.HorizontalDivider(
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                        thickness = 1.dp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Renter information
                    PropertyDetailRow(
                        icon = Icons.Filled.Person,
                        label = "Renter",
                        value = property.renterName ?: "No renter assigned"
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Subscriptions information
                    if (property.subscriptions.isEmpty()) {
                        PropertyDetailRow(
                            icon = Icons.Filled.ElectricalServices,
                            label = "Electricity Subscriptions",
                            value = "No subscriptions configured"
                        )
                    } else {
                        Text(
                            text = "Electricity Subscriptions:",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(start = 32.dp)
                        )

                        property.subscriptions.forEach { subscription ->
                            val billCount = subscription.electricityBills.size
                            val billSummary = if (billCount > 0) {
                                // Group bills by currency and calculate totals
                                val currencyTotals = subscription.electricityBills
                                    .groupBy { it.currency }
                                    .map { (currency, bills) ->
                                        "${bills.sumOf { it.amount }} ${currency.symbol}"
                                    }
                                    .joinToString(", ")
                                "$billCount bills, total: $currencyTotals"
                            } else {
                                "No bills recorded"
                            }

                            PropertyDetailRow(
                                icon = Icons.Filled.ElectricalServices,
                                label = subscription.name,
                                value = billSummary
                            )

                            if (subscription != property.subscriptions.last()) {
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}