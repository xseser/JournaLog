package com.example.journalog.viewmodels

sealed class NavigationEvent {
    object NavigateToOverview: NavigationEvent()
//    object NavigateToLogBook: NavigationEvent()
    object NavigateToLogin : NavigationEvent()
    object NavigateToRegistration : NavigationEvent()
    object NavigateToActiveLogBook : NavigationEvent()
}
