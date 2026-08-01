package com.example.gorestapi

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun UserDialog(isDialogShown: MutableState<Boolean>,
               name: String = "",
               email: String = "",
               gender: String = "",
               status: String = "",
               uid: Int? = null,
               action: (String, String, String, String, Int?) -> Unit) {
    var name = rememberTextFieldState(initialText = name)
    val email = rememberTextFieldState(initialText = email)
    var gender = rememberTextFieldState(initialText = gender)
    var status = rememberTextFieldState(initialText = status)

    Dialog(onDismissRequest = {
        isDialogShown.value = false
    }) {
        Card(modifier = Modifier.fillMaxWidth()
            .height(480.dp)
            .padding(20.dp)) {
            Column(modifier = Modifier.fillMaxSize().padding(25.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Add new user", fontSize = 24.sp)
                OutlinedTextField(
                    state = name
                    , modifier = Modifier.padding(bottom = 8.dp).padding(top = 8.dp))
                OutlinedTextField(
                    state = email
                    , modifier = Modifier.padding(bottom = 8.dp))
                OutlinedTextField(
                    state = gender
                    , modifier = Modifier.padding(bottom = 8.dp))
                OutlinedTextField(
                    state = status
                )
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround) {
                    Button(onClick = {
                        action(name.text.toString(), email.text.toString(),
                            gender.text.toString(), status.text.toString(), uid)
                        isDialogShown.value = false
                    }, modifier = Modifier.padding(vertical = 25.dp)) {
                        Text("Add")
                    }
                    Button(onClick = {
                        isDialogShown.value = false
                    }, modifier = Modifier.padding(vertical = 25.dp)) {
                        Text("Cancel")
                    }
                }
            }
        }
    }
}

