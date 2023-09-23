package com.example.journalog.viewmodels.logbook

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.journalog.database.LogBook
import com.example.journalog.database.LogBookDao
import com.example.journalog.database.LogDao
import com.example.journalog.datastore.DataStoreRepository
import com.example.journalog.model.User
import com.example.journalog.retrofit.ApiInterface
import com.example.journalog.retrofit.RetrofitClient
import com.example.journalog.viewmodels.NavigationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogBookViewModel @Inject constructor(
    private val logBookDao: LogBookDao,
    private val logDao: LogDao,
    private val dataStoreRepository: DataStoreRepository
): ViewModel() {

//    private val _logBooks = logBookDao.getAllLogBooks().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _state = MutableStateFlow(LogBookState())
    val state: StateFlow<LogBookState> = _state
//    val state = combine(_state, _logBooks) { state, logBooks ->
//        state.copy(
//            logBookList = logBooks
//        )
//    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), LogBookState())


//    private val _navigationEvent = MutableStateFlow<NavigationEvent?>(null)
//    val navigationEvent: StateFlow<NavigationEvent?> = _navigationEvent

    private val _navigationEvent = MutableStateFlow<NavigationEvent?>(null)
    val navigationEvent: StateFlow<NavigationEvent?> = _navigationEvent

    fun onNavigationCompleted() {
        _navigationEvent.value = null
    }

    init {
        onEvent(LogBookEvent.GetLogBooks)
    }

    fun onEvent(event: LogBookEvent) {
        when (event) {
            LogBookEvent.CreateLogBook -> {
                val name = state.value.name
//                var startTime = state.value.startTime
//
//                if(startTime == 0L) {
//                    startTime = System.currentTimeMillis()
//                }

//                val newLogBook = LogBook(
//                    name = name,
//                    startTime = startTime
//                )

                viewModelScope.launch {
                    var retrofit = RetrofitClient.getInstance()
                    var apiInterface = retrofit.create(ApiInterface::class.java)
                    viewModelScope.launch {
//                    logBookDao.deleteLogBook(state.value.logBookToDelete)
                        try {
                            println("zaczynam swirowac")
                            val userId: String = dataStoreRepository.getUserId()?.toString()?:""
                            val response= apiInterface.createLogBook(name, userId)
                            if (response.isSuccessful) {
                                println("zeswiroWALEM")
                                println(response.body())
                                //your code for handaling success response
                                onEvent(LogBookEvent.GetLogBooks)

                            } else {
                                println("no i ")
                            }
                        } catch (Ex: Exception) {
                            Log.e("Error", Ex.localizedMessage)
                        }
                    }
                }
                _state.update { it.copy(
                    name = "",
                    startTime = 0L,
                    isAddingLogBook = false
                ) }
            }
            is LogBookEvent.SetName -> {
                _state.update { it.copy(
                    name = event.name
                ) }
            }
            is LogBookEvent.SetStartingTime -> {
                _state.update { it.copy(
                    startTime = event.startingTime
                ) }
            }
            LogBookEvent.HideAddDialog -> {
                _state.update { it.copy(
                    isAddingLogBook = false
                ) }
            }
            LogBookEvent.ShowAddDialog -> {
                println("dialog :)")
                _state.update { it.copy(
                    isAddingLogBook = true
                ) }
            }

            is LogBookEvent.PrepareChosenLogBookDetails -> {
                var retrofit = RetrofitClient.getInstance()
                var apiInterface = retrofit.create(ApiInterface::class.java)
                viewModelScope.launch {
                    try {
                        println("zaczynam swirowac bez sensu a przeciez nikt mnie nie pytal")
                        val userId: String = dataStoreRepository.getUserId()?.toString()?:""
                        val response = apiInterface.getLogBookById(userId, event.id.toString())
                        if (response.isSuccessful) {
                            println("zeswiroWALEM")
                            println(response.body())
                            //your code for handaling success response
                            _state.update {
                                it.copy(
                                    id = event.id,
                                    name = response.body()!!.name,
                                    startTime = response.body()!!.startTime.toLong(),
                                    loading = false
                                )
                            }

                        } else {
                            println("no i ")
                        }
                    } catch (Ex: Exception) {
                        Log.e("Error", Ex.localizedMessage)
                    }
                }
//                val logBook: LogBook = logBookDao.getLogBookById(event.id)
//                _state.update { it.copy(
//                    id = logBook.id,
//                    name = logBook.name,
//                    startTime = logBook.startTime
//                ) }
            }

            is LogBookEvent.SetActiveLogBook -> {
                viewModelScope.launch {
                    var retrofit = RetrofitClient.getInstance()
                    var apiInterface = retrofit.create(ApiInterface::class.java)
                    viewModelScope.launch {
//                    logBookDao.deleteLogBook(state.value.logBookToDelete)
                        try {
                            println("zaczynam swirowac ustawiac KURWA WIDZE TO WIDZE Z CZYM PROBLEM")
                            val userId: String = dataStoreRepository.getUserId()?.toString() ?: ""
                            val response =
                                apiInterface.updateLogStatus(userId, event.id, true.toString())
                            if (response.isSuccessful) {
                                println("zeswiroWALEM")
                                println(response.body())
                                //your code for handaling success response
//                                onEvent(LogBookEvent.GetLogBooks)
                                _state.update { it.copy(
                                    id = event.id
                                ) }
                                _navigationEvent.value = NavigationEvent.NavigateToActiveLogBook

                            } else {
                                println("no i ")
                            }
                        } catch (Ex: Exception) {
                            Log.e("Error", Ex.localizedMessage?:"")
                        }
                    }
                }
            }

            is LogBookEvent.ShowDeleteDialog -> {
                _state.update { it.copy(
//                    id = event.logBook.id,
//                    name = event.logBook.name,
                    logBookToDelete = event.logBook,
                    isDeletingLogBook = true
                ) }
            }

            LogBookEvent.HideDeleteDialog -> {
                _state.update { it.copy(
                    logBookToDelete = null,
                    isDeletingLogBook = false
                ) }
            }

            is LogBookEvent.DeleteLogBook -> {
                var retrofit = RetrofitClient.getInstance()
                var apiInterface = retrofit.create(ApiInterface::class.java)
                viewModelScope.launch {
//                    logBookDao.deleteLogBook(state.value.logBookToDelete)
                    try {
                        println("zaczynam swirowac")
                        val userId: String = dataStoreRepository.getUserId()?.toString()?:""
                        val response= apiInterface.deleteLogBook(userId, state.value.logBookToDelete!!.id)
                        if (response.isSuccessful) {
                            println("zeswiroWALEM")
                            println(response.body())
                            //your code for handaling success response
                            onEvent(LogBookEvent.GetLogBooks)

                        } else {
                            println("no i ")
                        }
                    } catch (Ex: Exception) {
                        Log.e("Error", Ex.localizedMessage)
                    }
                }
//                viewModelScope.launch {
//                    logDao.deleteLogsByLogBookId(state.value.logBookToDelete.id)
//                }
                _state.update { it.copy(
//                    id = -1,
//                    name = "",
                    logBookToDelete = null,
                    isDeletingLogBook = false
                ) }
            }

            LogBookEvent.GetUser -> {
                println("halo")
                var retrofit = RetrofitClient.getInstance()
                var apiInterface = retrofit.create(ApiInterface::class.java)
                viewModelScope.launch {
                    try {
                        println("zaczynam swirowac")
                        val response= apiInterface.getUserById()
                        if (response.isSuccessful) {
                            println("zeswiroWALEM")
                            println(response.body())
                            //your code for handaling success response
                            _state.update {
                                it.copy(
                                    user = response.body()
                                )
                            }

                        } else {
                            println("no i ")
                        }
                    } catch (Ex: Exception) {
                        Log.e("Error", Ex.localizedMessage)
                    }
                }
            }

            LogBookEvent.GetLogBooks -> {
                _state.update { it.copy(
                    loading = true
                ) }
                var retrofit = RetrofitClient.getInstance()
                var apiInterface = retrofit.create(ApiInterface::class.java)
                viewModelScope.launch {
                    try {
                        println("zaczynam swirowac")
                        val userId: String = dataStoreRepository.getUserId()?.toString()?:""
                        val response= apiInterface.getLogBooks(userId, 10)
                        if (response.isSuccessful) {
                            println("zeswiroWALEM")
                            println(response.body())
                            //your code for handaling success response
                            _state.update {
                                it.copy(
                                    logBookList = response.body()!!,
                                    loading = false
                                )
                            }

                        } else {
                            println("no i ")
                        }
                    } catch (Ex: Exception) {
                        Log.e("Error", Ex.localizedMessage)
                    }
                }
            }
        }
    }
}