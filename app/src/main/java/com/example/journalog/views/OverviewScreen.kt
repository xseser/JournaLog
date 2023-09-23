package com.example.journalog.views

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.journalog.AddLogBookDialog
import com.example.journalog.DeleteLogBookDialog
import com.example.journalog.database.LogBook
import com.example.journalog.model.LogBookResponseModel
import com.example.journalog.viewmodels.logbook.LogBookEvent
import com.example.journalog.viewmodels.logbook.LogBookState
import java.text.DateFormat
import java.time.Instant
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun OverviewScreen(
    state: LogBookState,
    onEvent: (LogBookEvent) -> Unit,
//    onLogBookButtonClicked: (Int) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onEvent(LogBookEvent.ShowAddDialog)
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add LogBook"
                )
            }
        },

    ) {paddingValues ->
        val padding = paddingValues
        if (state.isAddingLogBook) {
            AddLogBookDialog(state = state, onEvent = onEvent)
        }

        if (state.isDeletingLogBook) {
            DeleteLogBookDialog(state = state, onEvent = onEvent)
        }

        val ptrState= rememberPullRefreshState(state.loading, {onEvent(LogBookEvent.GetLogBooks)})
        Box(Modifier.pullRefresh(ptrState, enabled = true)) {
//            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(all = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(state.logBookList) {logBook ->
//                        LogBookRow(logBook, onEvent, onLogBookButtonClicked)
                        LogBookRow(logBook, onEvent)
                    }
                }
//            }
            if (state.loading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.2F)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.wrapContentSize())
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogBookRow(
    logBook: LogBookResponseModel,
    onEvent: (LogBookEvent) -> Unit,
//    onLogBookButtonClicked: (Int) -> Unit
) {
//    val startTimeFormatted = (logBook.startTime / 1000).seconds.toString()
    val date = Date(logBook.startTime.toLong())
//    val date = Date(1687270828000L)
    val formattedDateTime = DateFormat.getDateTimeInstance().format(date)

    Surface (
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 4.dp,
        onClick = {
            onEvent(LogBookEvent.SetActiveLogBook(logBook.id))
            println("aktywowany logbook: ${logBook.id}")
//            onLogBookButtonClicked(logBook.id)
        }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column (
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text(
                    text = logBook.name,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = formattedDateTime,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
//            Row (
//                verticalAlignment = Alignment.CenterVertically
//            ){
//                Button(onClick = {
//                    //                        onEvent(LogBookEvent.PrepareChosenLogBookDetails(logBook.id))
//                    onEvent(LogBookEvent.SetActiveLogBook(logBook.id))
//                    onLogBookButtonClicked(logBook.id)
//                }) {
//                    Text(text = "Open")
//                }
                IconButton(
                    modifier = Modifier
                        .height(56.dp)
                        .align(Alignment.Bottom),
                    onClick = {
                        onEvent(LogBookEvent.ShowDeleteDialog(logBook))
                    }
                ) {
                    Icon(Icons.Rounded.Delete, "Delete notebook")
                }
//            }
        }
    }
}

@Preview
@Composable
fun LogBookRowPreview() {
    val logBookSample = LogBook(name = "Przykladowy logBook", startTime = 1687270828000L)
//    LogBookRow(logBook = logBookSample, onEvent = {}, onLogBookButtonClicked = {})
}

//@Preview
//@Composable
//fun OverviewScreenPreview() {
//    OverviewScreen(
//        state = LogBookState(),
//        onEvent = {},
//        onLogBookButtonClicked = {}
//    )
//}