package com.example.journalog.viewmodels.topappbar

import android.app.Application
import android.util.Log
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.journalog.JournaLogScreen
import com.example.journalog.database.LogBookDao
import com.example.journalog.datastore.DataStoreRepository
import com.example.journalog.retrofit.ApiInterface
import com.example.journalog.retrofit.RetrofitClient
import com.example.journalog.viewmodels.NavigationEvent
import com.example.journalog.viewmodels.logbook.LogBookState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class TopAppBarViewModel@Inject constructor(
    private val dao: LogBookDao,
    private val context: Application,
    private val dataStoreRepository: DataStoreRepository
): ViewModel() {

    val state = MutableStateFlow(TopAppBarState())

    private val _navigationEvent = MutableStateFlow<NavigationEvent?>(null)
    val navigationEvent: StateFlow<NavigationEvent?> = _navigationEvent

    fun onNavigationCompleted() {
        _navigationEvent.value = null
    }

    fun onEvent(event: TopAppBarEvent) {
        when (event) {
            TopAppBarEvent.OverviewScreenSelected -> {
                val username: String = runBlocking {
                    dataStoreRepository.getUsername()
                }?: ""
                state.update { it.copy(
//                    title = context.getString(JournaLogScreen.Overview.title)
                        title = "Hello, $username"
                ) }
            }
            TopAppBarEvent.ActiveLogBookSelected -> {
//                var retrofit = RetrofitClient.getInstance()
//                var apiInterface = retrofit.create(ApiInterface::class.java)
//                viewModelScope.launch {
//                    try {
//                        println("zaczynam swirowac top app bar")
//                        val userId: String = dataStoreRepository.getUserId()?.toString()?:""
//                        val response = apiInterface.getActiveLogBook(userId)
//                        if (response.isSuccessful) {
//                            println("zeswiroWALEM")
//                            println(response.body())
//                            //your code for handaling success response
//                            state.update { it.copy(
//                                title = response.body()!!.name
//                            ) }
//
//                        } else {
//                            println("no i ")
//                        }
//                    } catch (Ex: Exception) {
//                        Log.e("Error", Ex.localizedMessage)
//                    }
//                }

            }

            TopAppBarEvent.LogOut -> {
                viewModelScope.launch {
                    dataStoreRepository.clearUserData()
                    _navigationEvent.value = NavigationEvent.NavigateToLogin
                }
            }
        }
    }
}
