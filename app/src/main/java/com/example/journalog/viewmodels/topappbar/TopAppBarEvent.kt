package com.example.journalog.viewmodels.topappbar

sealed interface TopAppBarEvent {
//    data class setTitle(val name: String): TopAppBarEvent
    object OverviewScreenSelected : TopAppBarEvent
    object ActiveLogBookSelected : TopAppBarEvent
    object LogOut: TopAppBarEvent
//    data class ActiveLogBookSelected(val logBookId: Int) : TopAppBarEvent
}