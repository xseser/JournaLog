package com.example.journalog.viewmodels.log

import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.journalog.database.LogBook
import com.example.journalog.database.LogBookDao
import com.example.journalog.database.LogDao
import com.example.journalog.database.SingleLog
import com.example.journalog.datastore.DataStoreRepository
import com.example.journalog.retrofit.ApiInterface
import com.example.journalog.retrofit.RetrofitClient
import com.example.journalog.viewmodels.logbook.LogBookEvent
import com.example.journalog.viewmodels.logbook.LogBookState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogViewModel @Inject constructor(
    private val logBookDao: LogBookDao,
    private val logDao: LogDao,
    private val dataStoreRepository: DataStoreRepository
//    private val logBookState: LogBookState
//    private val currentLogBookId: Int
): ViewModel() {

//    private var logBookState: LogBookState = LogBookState()
//    private val _activeLogBook = logBookDao.getActiveLogBookAsFlow()
//        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), LogBook())
//    private val _logs = logDao.getAllLogsFromActiveLogBook()
//        .map { logs -> logs.map { it.toDisplayableLog(_activeLogBook.value.startTime) } }
//        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
//    private val _state = MutableStateFlow(LogState())
//    val state = combine(_state, _logs) { state, logs ->
//        state.copy(
//            logList = logs
//        )
//    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), LogState())

//    private val _activeLogBook = logBookDao.getActiveLogBookAsFlow()
//        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), LogBook())
//
//    private val _logs = logDao.getAllLogsFromActiveLogBook()
//        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList<SingleLog>())

    private val _state = MutableStateFlow(LogState())
    val state: StateFlow<LogState> = _state
//    private val _state = _activeLogBook.map { activeLogBook ->
//        LogState(anchorLogTime = activeLogBook.startTime)
//    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), LogState())

//    combine(_activeLogBook, _logs) { activeLogBook, logs ->
//        _state.value = _state.value.copy(anchorLogTime = activeLogBook.startTime)
//        logs.map { it.toDisplayableLog(activeLogBook.startTime) }
//    }

    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // TO BEDZIE POTRZEBNE
//    private val _currentStartTime = combine(_activeLogBook, _state) { activeLogBook, state ->
//        val newAnchorLogTime: Long = if (state.anchorLogTime == -1L) activeLogBook.startTime else state.anchorLogTime
//        state.copy(
//            startTime = activeLogBook.startTime,
//            anchorLogTime = newAnchorLogTime
//        )
//    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), LogState())

//    private val _displayableLogs = combine(_currentStartTime, _logs) { state, logs ->
//        logs.map { it.toDisplayableLog(state.anchorLogTime) }
//    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList<DisplayableLog>())

//    val state = combine(_state, _displayableLogs) { state, logs ->
//        state.copy(
//            logList = logs
//        )
//    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), LogState())

//    init {
//        onEvent(LogEvent.SetLogBookId(9))
//    }

    fun onEvent(event: LogEvent) {
        when (event) {
            LogEvent.SaveLog -> {
//                val time = state.value.time
                val content = state.value.content
                val logBookId = state.value.logBookId

                if(content.isBlank()) {
                    return
                }

                _state.update { it.copy(
                    loading = true
                ) }

//                val sdf = SimpleDateFormat("HH:mm:ss", Locale.ROOT)
                val newLogTime = System.currentTimeMillis()
//                val currentTime = sdf.format(Date())
//                val passedTimeMillis = newLogTime - state.value.startTime
//                val passedTimeSeconds = (passedTimeMillis / 1000).seconds
//                val time = "After $passedTimeSeconds ($currentTime)"


                val newLog = SingleLog(
                    logBookId = logBookId,
                    timeWhenSent = newLogTime,
                    content = content
                )
                viewModelScope.launch {
                    var retrofit = RetrofitClient.getInstance()
                    var apiInterface = retrofit.create(ApiInterface::class.java)
                    viewModelScope.launch {
                        try {
                            println("zaczynam swirowac i zapisywac")
                            val userId: String = dataStoreRepository.getUserId()?.toString()?:""
                            val response = apiInterface.createLog(userId, logBookId, content)
                            if (response.isSuccessful) {
                                println("zeswiroWALEM")
                                println(response.body())
                                //your code for handaling success response
                                onEvent(LogEvent.getLogs)

                            } else {
                                println("no i")
                            }
                        } catch (Ex: Exception) {
                            Log.e("Error", Ex.localizedMessage)
                        }
                    }
                }
                _state.update { it.copy(
                    time = "",
                    content = ""
                ) }
            }
            is LogEvent.SetTime -> {
                _state.update { it.copy(
                    time = event.time
                ) }
            }
            is LogEvent.SetContent -> {
                _state.update { it.copy(
                    content = event.content,
                    isAddingNewLog = event.content.isNotBlank()
                ) }
            }
            is LogEvent.SetLogBookId -> {
//                val activeLogBook = logBookDao.getLogBookById(event.logBookId)

                println("ej ale serio ${event.logBookId}")
                var retrofit = RetrofitClient.getInstance()
                var apiInterface = retrofit.create(ApiInterface::class.java)
                viewModelScope.launch {
                    try {
                        println("zaczynam swirowac TU AKURAT JEST")
                        val userId: String = dataStoreRepository.getUserId()?.toString()?:""
                        val response = apiInterface.getLogBookById(userId, event.logBookId.toString())
                        if (response.isSuccessful) {
                            println("zeswiroWALEM")
                            println(response.body())
                            //your code for handaling success response
                            _state.update {
                                it.copy(
                                    logBookId = event.logBookId,
                                    logBookName = response.body()!!.name,
                                    startTime = response.body()!!.startTime.toLong(),
                                    loading = false,
                                    needsSetup = false
                                )
                            }
                            onEvent(LogEvent.getLogs)
                        } else {
                            println("no i ")
                        }
                    } catch (Ex: Exception) {
                        Log.e("Error", Ex.localizedMessage)
                    }
                }

            }
//            is LogEvent.SetLogBookState -> {
//                logBookState = event.logBookState
////                val newLogs = logDao.getLogsByLogBookId(logBookState.id)
////                _state.update { it.copy(
////                    logList = newLogs
////                ) }
//            }
            LogEvent.ShowNewLogBubble -> {
                _state.update { it.copy(
                    isAddingNewLog = true
                ) }
            }

            LogEvent.HideNewLogBubble -> {
                _state.update { it.copy(
                    isAddingNewLog = false
                ) }
            }

            is LogEvent.ExportLogBook -> {
                val wholeLogBook = buildString {
                    append(state.value.logBookName)
                    appendLine();appendLine()
                    val logList = state.value.logList
                    for (index in logList.indices) {
                        val singleLog = logList[index]
                        append(singleLog.dispTimePassed)
                        appendLine()
                        append(singleLog.content)
                        if (index != logList.size - 1) {
                            appendLine();appendLine()
                        }
                    }
                }

                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_SUBJECT, state.value.logBookName)
                    putExtra(Intent.EXTRA_TEXT, wholeLogBook)
                }

                event.context.startActivity(
                    Intent.createChooser(
                        intent,
                        "Notebook export"
                    )
                )
            }

            is LogEvent.SetAnchor -> {
                println("kotwica")
//                val newAnchorTime = event.logTime
//                val updatedLogs = _displayableLogs.value.map { displayableLog ->
//                    displayableLog.updateDispTimePassed(newAnchorTime)
//                    return@map displayableLog
//                }
//                (_displayableLogs as MutableStateFlow).value = updatedLogs
                if (state.value.anchorLogId == event.logId) {
                    _state.update { it.copy(
                        anchorLogId = -1,
                        anchorLogTime = -1,
                    ) }
                }
                else {
                    _state.update { it.copy(
                        anchorLogId = event.logId,
                        anchorLogTime = event.logTime,
                    ) }
                }
                onEvent(LogEvent.getLogs)
//
//                _state.update { currentState ->
//                    currentState.copy(
//                        anchorLogId = event.logId,
//                        anchorLogTime = event.logTime,
//                        logList = currentState.logList.map { displayableLog ->
//                            return displayableLog.updateDispTimePassed(event.logTime)
//                        }
//                    )
//                }
            }

            LogEvent.getLogs -> {
                _state.update { it.copy(
                    loading = true
                ) }
                var retrofit = RetrofitClient.getInstance()
                var apiInterface = retrofit.create(ApiInterface::class.java)
                viewModelScope.launch {
                    try {
                        println("zaczynam swirowac z pojedynczymi logami")
                        val userId: String = dataStoreRepository.getUserId()?.toString()?:""
                        val response = apiInterface.getLogs(state.value.logBookId, userId)
                        if (response.isSuccessful) {
                            println("zeswiroWALEM")
                            println(response.body())
                            //your code for handaling success response
                            val timeRelativeBase = if (state.value.anchorLogId == -1) state.value.startTime else state.value.anchorLogTime
                            val displayableLogList: MutableList<DisplayableLog> = emptyList<DisplayableLog>().toMutableList()
                            response.body()!!.forEach { singleLogResponseModel -> displayableLogList.add(singleLogResponseModel.toDisplayableLog(timeRelativeBase)) }
                            _state.update {
                                it.copy(
                                    logList = displayableLogList,
                                    loading = false
                                )
                            }

                        } else {
                            println("no i")
                        }
                    } catch (Ex: Exception) {
                        Log.e("Error", Ex.localizedMessage)
                    }
                }
            }

            LogEvent.getCurrentTemperature -> {
                _state.update { it.copy(
                    loading = true
                ) }
                var retrofit = RetrofitClient.getInstance()
                var apiInterface = retrofit.create(ApiInterface::class.java)
                viewModelScope.launch {
                    try {
                        println("zaczynam swirowac z pogodą")
//                        val userId: String = dataStoreRepository.getUserId()?.toString()?:""
                        val response = apiInterface.getCurrentTemperature()
                        if (response.isSuccessful) {
                            println("zeswiroWALEM")
                            println(response.body())
                            //your code for handaling success response
//                            val timeRelativeBase = if (state.value.anchorLogId == -1) state.value.startTime else state.value.anchorLogTime
//                            val displayableLogList: MutableList<DisplayableLog> = emptyList<DisplayableLog>().toMutableList()
//                            response.body()!!.forEach { singleLogResponseModel -> displayableLogList.add(singleLogResponseModel.toDisplayableLog(timeRelativeBase)) }
                            _state.update {
                                it.copy(
                                    content = "temp: ${response.body()!!} C"
                                )
                            }
                            onEvent(LogEvent.SaveLog)

                        } else {
                            println("no i ")
                        }
                    } catch (Ex: Exception) {
                        Log.e("Error", Ex.localizedMessage)
                    }
                }
            }
        }
    }
}