package com.example.journalog

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.journalog.viewmodels.log.DisplayableLog
import com.example.journalog.viewmodels.log.LogEvent
import com.example.journalog.viewmodels.log.LogState
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveLogBookScreen(
    state: LogState,
    onEvent: (LogEvent) -> Unit
) {
//    val mContext = LocalContext.current
//    val startTime = state.startTime
    val lazyColumnListState = rememberLazyListState()

    println("ODSWIEZYLEM")

    Scaffold(
        topBar = {},
        content = { paddingValues ->
            Surface(
                modifier = Modifier.padding(paddingValues),
                color = MaterialTheme.colorScheme.background
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TimerOrLoading(state.startTime)

                    SimpleDivider()

                    Box(Modifier.weight(1f)) {
                        DisplayAllLogs(
                            state = state,
                            onEvent = onEvent,
                            lazyColumnListState = lazyColumnListState
                        )
                    }

                    SimpleDivider()

                    NewEntryBar(startTime = state.startTime, state = state, onEvent = onEvent)
//                    NewEntryBar(
//                        startTime,
//                        onMessageChanged = {
////                            messagesList.add(it)
//                            addLogInDB(mContext, it, homeViewModel)
//                            scope.launch {
//                                lazyColumnListState.scrollToItem(0)
//                            }
//                        }
//                    )
                }
            }
        }
    )
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

@Composable
fun DisplayAllLogs(
    state: LogState,
    onEvent: (LogEvent) -> Unit,
    lazyColumnListState: LazyListState
) {
    LazyColumn (
        modifier = Modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        reverseLayout = true,
        state = lazyColumnListState
    ) {
        items(state.logList.asReversed()) { singleLog ->
            DisplayLog(singleLog, state, onEvent)
        }
    }
}

@Composable
fun SimpleDivider() {
    Divider(
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier
            .fillMaxWidth()
            .width(1.dp)
    )
}

@Composable
private fun TimerOrLoading(startTime: Long) {
    if (startTime == 0L) {
        // Display loading indicator
        CircularProgressIndicator() // or any other loading UI
    } else {
        Timer(startTime)
    }
}

@Composable
private fun Timer(startTime: Long) {
    var messageContent by remember { mutableStateOf("") }
    LaunchedEffect(key1 = Unit){
        while(true){
            val newLogTime = System.currentTimeMillis()
            val passedTimeMillis = newLogTime - startTime
            val passedTimeSeconds = (passedTimeMillis / 1000).seconds
            messageContent = "$passedTimeSeconds"
            delay(1000)
        }
    }
    Column(modifier= Modifier.fillMaxWidth()) {
        Text(
            messageContent,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 40.sp)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DisplayLog(
    singleLog: DisplayableLog,
    state: LogState,
    onEvent: (LogEvent) -> Unit
) {
    val isAnchor = (singleLog.id == state.anchorLogId)
    val surfaceColor = if (isAnchor) Color(0xFF147C25) else MaterialTheme.colorScheme.secondary
    val elapsedTimeTextColor = if (isAnchor) Color(0xFF147C25) else MaterialTheme.colorScheme.primary
    val absoluteTimeTextColor = if (isAnchor) Color(0xFF147C25) else MaterialTheme.colorScheme.secondary

    Column (modifier = Modifier.padding(bottom = 8.dp)) {
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
//            val endIdx: Int = singleLog.time.indexOf("(")
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = if (isAnchor) "ANCHORED HERE" else singleLog.dispTimePassed,
//                text = singleLog.time.substring(0, endIdx),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = elapsedTimeTextColor
            )
            Text(
                text = singleLog.dispTimeAbsolute,
                fontSize = 12.sp,
                color = absoluteTimeTextColor
            )
        }
        //initial height set at 0.dp
//        var rowWidth by remember { mutableStateOf(0.dp) }

        // get local density from composable
//        val density = LocalDensity.current



        Box(
            modifier = Modifier
                .fillMaxWidth()
//                .onGloballyPositioned {
//                    rowWidth = with(density) {
//                        it.size.width.toDp()
//                    }
//                },
        ) {
            Divider(
                modifier = Modifier
                    .padding(start = 8.dp),
                color = surfaceColor
            )
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
//                val secondaryColor = MaterialTheme.colorScheme.secondary
//                val surfaceColor = remember { mutableStateOf(secondaryColor) }
//                val isAnchor = ()
                Surface(
                    modifier = Modifier
                        .combinedClickable(
                            onLongClick = {
//                                surfaceColor.value = Color.Red
                                onEvent(LogEvent.SetAnchor(singleLog.id, singleLog.timeWhenSent))
                            }
                        ){},
//                        .widthIn(max = rowWidth - 8.dp),
                    shape = RoundedCornerShape(
                        topStart = 8.dp,
                        topEnd = 0.dp,
                        bottomEnd = 8.dp,
                        bottomStart = 8.dp
                    ),
                    color = surfaceColor
//                    color = MaterialTheme.colorScheme.secondary,
                ) {
                    Text(
                        text = singleLog.content,
                        modifier = Modifier.padding(8.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Surface(
                    modifier = Modifier.size(8.dp),
                    shape = CutCornerShape(
                        topStart = 0.dp,
                        topEnd = 0.dp,
                        bottomEnd = 8.dp,
                        bottomStart = 0.dp
                    ),
                    color = surfaceColor
                ) {}
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewEntryBar(
    startTime: Long,
    state: LogState,
    onEvent: (LogEvent) -> Unit
//    onMessageChanged: (SingleLog) -> Unit
) {
    var newEntryContent by remember { mutableStateOf("") }
    val context = LocalContext.current
    Surface(
        modifier = Modifier.padding(8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                modifier = Modifier
                    .height(56.dp)
                    .align(Alignment.Bottom),
                onClick = {
                    onEvent(LogEvent.ExportLogBook(context))
                }
            ) {
                Icon(Icons.Rounded.Share, "Download notebook")
            }
            IconButton(
                modifier = Modifier
                    .height(56.dp)
                    .align(Alignment.Bottom),
                onClick = {
                    onEvent(LogEvent.getCurrentTemperature)
                }
            ) {
                Icon(Icons.Rounded.Star, "Get current temperature")
            }
            OutlinedTextField(
                value = state.content,
                onValueChange = { onEvent(LogEvent.SetContent(it)) },
                label = { Text("New entry") },
                modifier = Modifier.weight(1f)
            )
            IconButton(
                modifier = Modifier
                    .height(56.dp)
                    .align(Alignment.Bottom),
                onClick = {
//                    val sdf = SimpleDateFormat("HH:mm:ss", Locale.ROOT)
//                    val newLogTime = System.currentTimeMillis()
//                    val currentTime = sdf.format(Date())
//                    val passedTimeMillis = newLogTime - startTime
//                    val passedTimeSeconds = (passedTimeMillis / 1000).seconds
//                    onEvent(LogEvent.SetTime("After $passedTimeSeconds ($currentTime)"))
                    onEvent(LogEvent.SaveLog)
//                    val newLog = SingleLog(
//                        time = "After $passedTimeSeconds ($currentTime)",
//                        content = newEntryContent
//                    )
                    newEntryContent = ""
//                    onMessageChanged(newLog)
                }
            ) {
                Icon(Icons.Rounded.Send, "Add log")
            }
        }
    }
}

//@Preview
//@Composable
//fun ActiveLogBookScreenPreview() {
//    val logList: List<SingleLog> = listOf(
//        SingleLog(1,0,
//            "After 21m 37s (16:20:21)",
//            "Przykładowa napisana już wiadomość"),
//        SingleLog(2,0,
//            "After 22m 28s (16:27:21)",
//            "Długa wiadomość, która zajmie więcej miejsca i pokaże jak szeroki może być bąbelek z wiadomością.")
//    )
//
//    val logState = LogState(
//        logList = logList,
//        logBookId = 0,
//        logBookName = "Przykładowy LogBook",
//        startTime = 0L,
//        time = "After 21m 37s (16:20:21)",
//        content = "Wiadomość w trakcie tworzenia",
//        isAddingNewLog = true
//    )
//    ActiveLogBookScreen(state = logState, onEvent = {})
//
//}