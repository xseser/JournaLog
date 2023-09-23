package com.example.journalog.viewmodels.log

import android.content.Context

sealed interface LogEvent {
    object SaveLog: LogEvent
    data class SetContent(val content: String): LogEvent
    data class SetTime(val time: String): LogEvent
    data class SetLogBookId(val logBookId: Int): LogEvent
    data class ExportLogBook(val context: Context) : LogEvent
    //    data class SetLogBookState(val logBookState: LogBookState): LogEvent
    object ShowNewLogBubble: LogEvent
    object HideNewLogBubble: LogEvent
    data class SetAnchor(val logId: Int, val logTime: Long): LogEvent
    object getLogs: LogEvent
    object getCurrentTemperature: LogEvent
}