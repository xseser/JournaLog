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
fun AddLogBookDialog(
    state: LogBookState,
    onEvent: (LogBookEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            onEvent(LogBookEvent.HideAddDialog)
        },
        title = { Text(text = "Add LogBook") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = state.name,
                    onValueChange = {
                        onEvent(LogBookEvent.SetName(it))
                    },
                    placeholder = {
                        Text(text = "LogBook name")
                    }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                onEvent(LogBookEvent.CreateLogBook)
            }) {
                Text(text = "Create")
            }
        },
        dismissButton = {
            Button(onClick = {
                onEvent(LogBookEvent.HideAddDialog)
            }) {
                Text(text = "Cancel")
            }
        }
    )
}