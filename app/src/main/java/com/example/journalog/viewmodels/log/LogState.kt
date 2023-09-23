package com.example.journalog.viewmodels.log

import com.example.journalog.database.SingleLog
import com.example.journalog.model.User

data class LogState(
    val logList: List<DisplayableLog> = emptyList(),
    val logBookId: Int = 0,
    val logBookName: String = "",
    val startTime: Long = 0L,
    val time: String = "",
    val content: String = "",
    val isAddingNewLog: Boolean = false,
    val anchorLogId: Int = -1,
    val anchorLogTime: Long = -1,
    val user: User? = null,
    val loading: Boolean = false,
    val needsSetup: Boolean = true
)
