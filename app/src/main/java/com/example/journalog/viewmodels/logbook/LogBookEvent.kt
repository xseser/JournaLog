package com.example.journalog.viewmodels.logbook

import com.example.journalog.database.LogBook
import com.example.journalog.model.LogBookResponseModel

sealed interface LogBookEvent {
    object CreateLogBook: LogBookEvent
    data class SetName(val name: String): LogBookEvent
    data class SetStartingTime(val startingTime: Long): LogBookEvent
    object ShowAddDialog: LogBookEvent
    object HideAddDialog: LogBookEvent
    data class PrepareChosenLogBookDetails(val id: Int): LogBookEvent
    data class SetActiveLogBook(val id: Int): LogBookEvent
    data class ShowDeleteDialog(val logBook: LogBookResponseModel): LogBookEvent
    object HideDeleteDialog: LogBookEvent
    object DeleteLogBook: LogBookEvent
    object GetUser: LogBookEvent
    object GetLogBooks: LogBookEvent
//    data class DeleteLogBook(val logBook: LogBook): LogBookEvent
}