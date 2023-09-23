package com.example.journalog.viewmodels.login

sealed interface AuthEvent {
    data class SetUsername(val username: String): AuthEvent
    data class SetPassword(val password: String): AuthEvent
    object AttemptLogin: AuthEvent
    object NavigateToRegistration: AuthEvent
    object NavigateToLogin: AuthEvent
    data class SetPasswordRepeated(val passwordRepeated: String): AuthEvent
    object AttemptRegistration: AuthEvent
}