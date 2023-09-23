package com.example.journalog.viewmodels.login

data class AuthState(
    val username: String = "",
    val password: String = "",
    val passwordRepeated: String = "",
    val error: String? = null,
    val loading: Boolean = false,
)
