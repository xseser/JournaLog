@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.example.journalog

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.journalog.viewmodels.NavigationEvent
import com.example.journalog.viewmodels.logbook.LogBookViewModel
import com.example.journalog.viewmodels.log.LogEvent
import com.example.journalog.viewmodels.log.LogViewModel
import com.example.journalog.viewmodels.login.AuthViewModel
import com.example.journalog.viewmodels.topappbar.TopAppBarEvent
import com.example.journalog.viewmodels.topappbar.TopAppBarState
import com.example.journalog.viewmodels.topappbar.TopAppBarViewModel
import com.example.journalog.views.LoginScreen
import com.example.journalog.views.OverviewScreen
import com.example.journalog.views.RegistrationScreen


enum class JournaLogScreen(@StringRes val title: Int, val route: String) {
    Login(title = R.string.app_name, route = "Login"),
    Registration(title=R.string.app_name, route = "Registration"),
    Overview(title = R.string.app_name, route = "Overview"),
    ActiveLogBook(title = R.string.active_log_book, route = "ActiveLogBook/{logBookId}")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournaLogAppBar(
//    currentScreen: JournaLogScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    state: TopAppBarState,
    modifier: Modifier = Modifier,
    onEvent: (TopAppBarEvent) -> Unit
) {
    TopAppBar(
        title = {
            Text(state.title)
//            if (currentScreen == JournaLogScreen.ActiveLogBook) {
//                Text(state.title)
////                Text("Test chwilowy")
//            }
//            else {
//                Text(stringResource(currentScreen.title))
//            }
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = {onEvent(TopAppBarEvent.LogOut)}) {
                Icon(
                    imageVector = Icons.Filled.ExitToApp,
                    contentDescription = null
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournaLogApp(
//    logBookViewModel: LogBookViewModel,
//    db: JournaLogRoomDatabase,
//    logViewModel: LogViewModel,
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
//    val currentScreen = JournaLogScreen.valueOf(
//        backStackEntry?.destination?.route ?: JournaLogScreen.Overview.name
//    )

    val topAppBarViewModel = hiltViewModel<TopAppBarViewModel>()
    val topAppBarState by topAppBarViewModel.state.collectAsState()

//    val currentScreen = JournaLogScreen.values().firstOrNull {
//        it.route == backStackEntry?.destination?.route } ?: JournaLogScreen.Overview

    Scaffold(

        topBar = {
            if (backStackEntry?.destination?.route != null &&
                backStackEntry?.destination?.route != JournaLogScreen.Login.name &&
                backStackEntry?.destination?.route != JournaLogScreen.Registration.name) {

                val navigationEvent by topAppBarViewModel.navigationEvent.collectAsState()

                LaunchedEffect(navigationEvent) {
                    println("nav event")
                    when (navigationEvent) {
                        is NavigationEvent.NavigateToLogin -> {
                            navController.navigate(JournaLogScreen.Login.name) {
                                // Clear back stack if needed
                                popUpTo(JournaLogScreen.Login.name) { inclusive = true }
                            }
                            topAppBarViewModel.onNavigationCompleted()
                        }

                        else -> {}
                    }
                }

                JournaLogAppBar(
//                currentScreen = currentScreen,
                    canNavigateBack = navController.previousBackStackEntry != null,
                    navigateUp = { navController.navigateUp() },
                    state = topAppBarState,
                    onEvent = topAppBarViewModel::onEvent
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = JournaLogScreen.Login.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = JournaLogScreen.Login.name) {
                val authViewModel = hiltViewModel<AuthViewModel>()
                val loginState by authViewModel.state.collectAsState()
                val navigationEvent by authViewModel.navigationEvent.collectAsState()

                LaunchedEffect(navigationEvent) {
                    println("nav event")
                    when (navigationEvent) {
                        is NavigationEvent.NavigateToOverview -> {
                            navController.navigate(JournaLogScreen.Overview.name) {
                                // Clear back stack if needed
                                popUpTo(JournaLogScreen.Login.name) { inclusive = true }
                            }
                            authViewModel.onNavigationCompleted()
                        }
                        is NavigationEvent.NavigateToRegistration -> {
                            println("halo")
                            navController.navigate(JournaLogScreen.Registration.name)
                            authViewModel.onNavigationCompleted()
                        }
                        else -> {
                            println("else")
                        }
                    }
                }

                LoginScreen(
                    state = loginState,
                    onEvent = authViewModel::onEvent
                )
            }
            composable(route = JournaLogScreen.Registration.name) {
                val authViewModel = hiltViewModel<AuthViewModel>()
                val loginState by authViewModel.state.collectAsState()
                val navigationEvent by authViewModel.navigationEvent.collectAsState()

                LaunchedEffect(navigationEvent) {
                    when (navigationEvent) {
                        is NavigationEvent.NavigateToOverview -> {
                            navController.navigate(JournaLogScreen.Overview.name) {
                                // Clear back stack if needed
                                popUpTo(JournaLogScreen.Login.name) { inclusive = true }
                            }
                            authViewModel.onNavigationCompleted()
                        }
                        NavigationEvent.NavigateToLogin -> {
                            navController.navigate(JournaLogScreen.Login.name)
                            authViewModel.onNavigationCompleted()
                        }
                        else -> {}
                    }
                }

                RegistrationScreen(
                    state = loginState,
                    onEvent = authViewModel::onEvent
                )
            }
            composable(route = JournaLogScreen.Overview.name) {
                topAppBarViewModel.onEvent(TopAppBarEvent.OverviewScreenSelected)

                val logBookViewModel = hiltViewModel<LogBookViewModel>()
                val logBookState by logBookViewModel.state.collectAsState()
                val navigationEvent by logBookViewModel.navigationEvent.collectAsState()

                LaunchedEffect(navigationEvent) {
                    println("nav event")
                    when (navigationEvent) {
                        is NavigationEvent.NavigateToActiveLogBook -> {
                            navController.navigate("${JournaLogScreen.ActiveLogBook.name}/${logBookState.id}") {
                                // Clear back stack if needed
//                                popUpTo(JournaLogScreen.Login.name) { inclusive = true }
                            }
                            logBookViewModel.onNavigationCompleted()
                            topAppBarViewModel.onEvent(TopAppBarEvent.ActiveLogBookSelected)
                        }
                        else -> {
                            println("else")
                        }
                    }
                }

                OverviewScreen(
                    state = logBookState,
                    onEvent = logBookViewModel::onEvent,
//                    onLogBookButtonClicked = {logBookId ->
//                        println("nawiguje do logbooka ${logBookId}")
//                        navController.navigate("${JournaLogScreen.ActiveLogBook.name}/$logBookId")
//                    },

//                    quantityOptions = DataSource.quantityOptions,
//                    onNextButtonClicked = {
//                        viewModel.setQuantity(it)
//                        navController.navigate(CupcakeScreen.Flavor.name)
//                    }
                )
            }
            composable(route = "${JournaLogScreen.ActiveLogBook.name}/{logBookId}") {

                val logBookId = backStackEntry?.arguments?.getString("logBookId")?.toIntOrNull()
                println("znawigowałem do ${logBookId}")
                val logViewModel = hiltViewModel<LogViewModel>()
//                logBookId?.let {
//                    logViewModel.onEvent(LogEvent.SetLogBookId(it))
//
//                }
                val logState by logViewModel.state.collectAsState()

                if (logState.needsSetup) {
                    logViewModel.onEvent(LogEvent.SetLogBookId(logBookId!!))
                }

                ActiveLogBookScreen(
                    state = logState,
                    onEvent = logViewModel::onEvent
                )
            }
        }
    }
}