package com.example.journalog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
//import androidx.compose.runtime.livedata.observeAsState
import com.example.journalog.ui.theme.JournaLogTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JournaLogTheme {
                JournaLogApp()
//                val state by viewModel.state.collectAsState()
//                ActiveLogBookScreen(state = state, onEvent = viewModel::onEvent)
            }
        }
    }
}
