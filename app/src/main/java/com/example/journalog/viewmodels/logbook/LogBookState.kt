package com.example.journalog.viewmodels.logbook

import com.example.journalog.database.LogBook
import com.example.journalog.model.LogBookResponseModel
import com.example.journalog.model.User

data class LogBookState(
    val logBookList: List<LogBookResponseModel> = emptyList(),
    val id: Int = 0,
    val name: String = "",
    val startTime: Long = 0,
    val isAddingLogBook: Boolean = false,
    val isDeletingLogBook: Boolean = false,
    val logBookToDelete: LogBookResponseModel? = null,
    val user: User? = null,
    val loading: Boolean = false,
)
