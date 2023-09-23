package com.example.journalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.journalog.viewmodels.logbook.LogBookEvent
import com.example.journalog.viewmodels.logbook.LogBookState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteLogBookDialog(
    state: LogBookState,
    onEvent: (LogBookEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            onEvent(LogBookEvent.HideDeleteDialog)
        },
        title = { Text(text = "Delete LogBook") },
        text = {
            Text(text = "Are you sure you want to delete ${state.logBookToDelete?.name}?")
        },
        confirmButton = {
            Button(onClick = {
                onEvent(LogBookEvent.DeleteLogBook)
            }) {
                Text(text = "Delete")
            }
        },
        dismissButton = {
            Button(onClick = {
                onEvent(LogBookEvent.HideDeleteDialog)
            }) {
                Text(text = "Cancel")
            }
        }
    )
}