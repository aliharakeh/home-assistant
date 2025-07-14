package com.example.homeassistant.ui.components.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ConfirmationDialog(
    title: String,
    message: String,
    filterFromDate: LocalDate? = null,
    filterToDate: LocalDate? = null,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")

    val fullMessage = buildString {
        append(message)
        if (filterFromDate != null || filterToDate != null) {
            append("\n\nFilter applied:")
            if (filterFromDate != null) {
                append("\nFrom: ${filterFromDate.format(dateFormatter)}")
            }
            if (filterToDate != null) {
                append("\nTo: ${filterToDate.format(dateFormatter)}")
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text(fullMessage) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}