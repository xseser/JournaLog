package com.example.journalog.viewmodels.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.journalog.database.LogBookDao
import com.example.journalog.database.LogDao
import com.example.journalog.datastore.DataStoreRepository
import com.example.journalog.model.AuthRequestModel
import com.example.journalog.retrofit.ApiInterface
import com.example.journalog.retrofit.RetrofitClient
import com.example.journalog.viewmodels.NavigationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val logBookDao: LogBookDao,
    private val logDao: LogDao,
    private val dataStoreRepository: DataStoreRepository
): ViewModel() {

    val state = MutableStateFlow(AuthState())

    private val _navigationEvent = MutableStateFlow<NavigationEvent?>(null)
    val navigationEvent: StateFlow<NavigationEvent?> = _navigationEvent

    fun onNavigationCompleted() {
        _navigationEvent.value = null
    }

    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.SetUsername -> {
                state.update { it.copy(
                    username = event.username
                ) }
            }

            is AuthEvent.SetPassword -> {
                state.update { it.copy(
                    password = event.password
                ) }
            }

            AuthEvent.AttemptLogin -> {
                val authRequestModel: AuthRequestModel = AuthRequestModel(state.value.username, state.value.password)

                state.update { it.copy(
                    loading = true
                ) }

                var retrofit = RetrofitClient.getInstance()
                var apiInterface = retrofit.create(ApiInterface::class.java)
                viewModelScope.launch {
                    try {
                        println("zaczynam swirowac")
                        val response = apiInterface.loginUser(authRequestModel)
                        println("no i co?")
                        if (response.isSuccessful) {
                            println("zeswiroWALEM")
                            response.body()?.let {
                                println(it.id)
                                dataStoreRepository.putUsername(it.name)
                                dataStoreRepository.putUserId(it.id)
                            }
                            _navigationEvent.value = NavigationEvent.NavigateToOverview
                            //your code for handaling success response
//                            _state.update {
//                                it.copy(
//                                    user = response.body()
//                                )
//                            }

                        } else {
                            state.update { it.copy(
                                username = "",
                                password = "",
                                loading = false,
                                error = "Incorrect username or password"
                            ) }
                        }
                    } catch (Ex: Exception) {
                        Log.e("Error", Ex.localizedMessage)
                        state.update { it.copy(
                            loading = false,
                            error = "Something went wrong. Try again later."
                        ) }
                    }
                }
            }

            AuthEvent.NavigateToRegistration -> {
                _navigationEvent.value = NavigationEvent.NavigateToRegistration
            }

            AuthEvent.NavigateToLogin -> {
                _navigationEvent.value = NavigationEvent.NavigateToLogin
            }

            is AuthEvent.SetPasswordRepeated -> {
                state.update { it.copy(
                    passwordRepeated = event.passwordRepeated
                ) }
            }

            AuthEvent.AttemptRegistration -> {
                if (state.value.password != state.value.passwordRepeated) {
                    state.update { it.copy(
                        username = "",
                        password = "",
                        error = "Passwords don't match."
                    ) }
                }
                else {
                    val authRequestModel: AuthRequestModel = AuthRequestModel(state.value.username, state.value.password)

                    state.update { it.copy(
                        loading = true
                    ) }

                    var retrofit = RetrofitClient.getInstance()
                    var apiInterface = retrofit.create(ApiInterface::class.java)
                    viewModelScope.launch {
                        try {
                            println("zaczynam swirowac")
                            val response = apiInterface.registerUser(authRequestModel)
                            println("no i co?")
                            if (response.isSuccessful) {
                                println("zeswiroWALEM")
                                response.body()?.let { println(it.id) }
                                response.body()?.let {
                                    println(it.id)
                                    dataStoreRepository.putUsername(it.name)
                                    dataStoreRepository.putUserId(it.id)
                                }

                                _navigationEvent.value = NavigationEvent.NavigateToOverview
                                //your code for handaling success response
//                            _state.update {
//                                it.copy(
//                                    user = response.body()
//                                )
//                            }

                            } else {
                                state.update { it.copy(
                                    username = "",
                                    password = "",
                                    loading = false,
                                    error = "Incorrect username or password"
                                ) }
                            }
                        } catch (Ex: Exception) {
                            Log.e("Error", Ex.localizedMessage)
                            state.update { it.copy(
                                loading = false,
                                error = "Something went wrong. Try again later."
                            ) }
                        }
                    }
                }
            }

        }
    }
}
