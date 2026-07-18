package com.example.gorestapi

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun UserList(backStack: SnapshotStateList<Any>, mainVM: MainViewModel = viewModel()) {
    val users = mainVM.users.collectAsState().value
    var isAddNewUserDialogShown = remember { mutableStateOf(false) }

    @Composable
    fun AddNewUserDialog(isDialogShown: MutableState<Boolean>, mainVM: MainViewModel) {
        var name = rememberTextFieldState()
        val email = rememberTextFieldState()
        var gender = rememberTextFieldState()
        var status = rememberTextFieldState()

        Dialog(onDismissRequest = {
            isAddNewUserDialogShown.value = false
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
                            mainVM.addUser(
                                user = name.text.toString(),
                                email = email.text.toString(),
                                gender = gender.text.toString(),
                                status = status.text.toString())
                            isDialogShown.value = false
                        }, modifier = Modifier.padding(vertical = 25.dp)) {
                            Text("Add")
                        }
                        Button(onClick = {
                            isAddNewUserDialogShown.value = false
                        }, modifier = Modifier.padding(vertical = 25.dp)) {
                            Text("Cancel")
                        }
                    }
                }
            }
        }
    }

    Scaffold() { innerPadding ->
        Box {
            if (isAddNewUserDialogShown.value == true) {
                AddNewUserDialog(isDialogShown = isAddNewUserDialogShown, mainVM = mainVM)
            } else {
                Column(verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    LazyColumn() {
                        items(items = users) {
                            Column(modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .padding(bottom = 20.dp)
                                .clickable {
                                    backStack.add(Routes.UserDetails(mainVM = mainVM,
                                        uid = it.id ?: 1))
                                }) {
                                Card(modifier = Modifier
                                    .padding(horizontal = 20.dp)
                                    .padding(vertical = 5.dp)
                                    .fillMaxWidth()) {
                                    Column(modifier = Modifier
                                        .padding(vertical = 12.dp)
                                        .padding(horizontal = 10.dp)) {
                                        Text(it.name,
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.Bold,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.fillMaxWidth())
                                        Text(it.email,
                                            textAlign = TextAlign.Center,
                                            fontSize = 20.sp,
                                            modifier = Modifier.fillMaxWidth())
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }
                Row(horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier.padding(35.dp).fillMaxSize()) {
                    FloatingActionButton(onClick = {
                        isAddNewUserDialogShown.value = !isAddNewUserDialogShown.value
                    }) {
                        Icon(Icons.Filled.Add, "")
                    }
                }
            }
        }
    }
}


