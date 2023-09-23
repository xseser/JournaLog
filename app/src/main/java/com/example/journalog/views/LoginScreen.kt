package com.example.journalog.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.journalog.viewmodels.login.AuthEvent
import com.example.journalog.viewmodels.login.AuthState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    state: AuthState,
    onEvent: (AuthEvent) -> Unit
) {
    Scaffold { paddingValues ->
        val padding = paddingValues

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "JournaLog",
                style = TextStyle(
                    fontFamily = FontFamily.Cursive, // Use system's serif font
                    fontSize = 60.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0D8931)
                ),
                modifier = Modifier
                    .padding(16.dp)
                    .padding(bottom = 36.dp)
            )
            OutlinedTextField(
                value = state.username,
                onValueChange = { onEvent(AuthEvent.SetUsername(it)) },
                label = { Text("Username") },
            )
            OutlinedTextField(
                value = state.password,
                onValueChange = { onEvent(AuthEvent.SetPassword(it)) },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation()
            )

            Button(onClick = {
                println("loguje sie: ${state.username}, ${state.password}")
                onEvent(AuthEvent.AttemptLogin)
            }) {
                Text("Login")
            }

            Text(state.error?: "", color = Color.Red)

            OutlinedButton(onClick = { onEvent(AuthEvent.NavigateToRegistration) }) {
                Text(text = "Create new account")
            }
        }

        if (state.loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.2F)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.wrapContentSize())
            }
        }
    }
}
