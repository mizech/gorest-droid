package com.example.gorestapi

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun UserDialog(isDialogShown: MutableState<Boolean>,
               name: String = "",
               email: String = "",
               gender: Gender = Gender.MALE,
               status: Status = Status.ACTIVE,
               uid: Int? = null,
               action: (String, String, String, String, Int?) -> Unit) {
    var name = rememberTextFieldState(initialText = name)
    val email = rememberTextFieldState(initialText = email)
    var selectedGender by remember { mutableStateOf(gender) }
    var status by remember { mutableStateOf(status) }

    var isGenderExpanded by remember { mutableStateOf(false) }
    var isStatusExpanded by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = {
        isDialogShown.value = false
    }) {
        Card(modifier = Modifier.fillMaxWidth()
            .height(540.dp)
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
                Box(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()) {
                        Text(text = "Gender: ${selectedGender.label}")
                        IconButton(onClick = { isGenderExpanded = !isGenderExpanded }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "")
                        }
                    }
                    DropdownMenu(expanded = isGenderExpanded, onDismissRequest = {
                        isGenderExpanded = false
                    }) {
                        DropdownMenuItem(text = {
                            Text(Gender.MALE.label)
                        }, onClick = {
                            selectedGender = Gender.MALE
                            isGenderExpanded = false
                        })
                        DropdownMenuItem(text = {
                            Text(Gender.FEMALE.label)
                        }, onClick = {
                            selectedGender = Gender.FEMALE
                            isGenderExpanded = false
                        })
                    }
                }
                Box(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()) {
                        Text("Status: ${status.label}")
                        IconButton(onClick = { isStatusExpanded = !isStatusExpanded }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "")
                        }
                    }
                    DropdownMenu(expanded = isStatusExpanded, onDismissRequest = {
                        isStatusExpanded = false
                    }) {
                        DropdownMenuItem(text = {
                            Text(Status.ACTIVE.label)
                        }, onClick = {
                            status = Status.ACTIVE
                        })
                        DropdownMenuItem(text = {
                            Text(Status.INACTIVE.label)
                        }, onClick = {
                            status = Status.INACTIVE
                        })
                    }
                }
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround) {
                    Button(onClick = {
                        action(name.text.toString(), email.text.toString(),
                            selectedGender.label, status.label, uid)
                        isDialogShown.value = false
                    }, modifier = Modifier.padding(vertical = 25.dp)) {
                        Text(if (uid == null) "Add" else "Edit")
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

