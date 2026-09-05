package com.example.gorestapi

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun UserList(backStack: SnapshotStateList<Any>, mainVM: MainViewModel = viewModel()) {
    val users = mainVM.users.collectAsState().value
    var isAddNewUserDialogShown = remember { mutableStateOf(false) }
    val context = LocalContext.current

    Scaffold(topBar = {
        TopAppBar(title = { Text("User list") })
    }) { innerPadding ->
        Box {
            if (isAddNewUserDialogShown.value) {
                UserDialog(isDialogShown = isAddNewUserDialogShown, mainVM = mainVM) {
                    name, email, gender, status, _ ->
                    mainVM.addUser(
                        userName = name,
                        email = email,
                        gender = gender,
                        status = status)
                }
            } else {
                Column(verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    if (mainVM.isLoading.value) {
                        Row(modifier = Modifier.fillMaxWidth().padding(top = 80.dp),
                            horizontalArrangement = Arrangement.Center) {
                            CircularProgressIndicator()
                        }
                    } else if (mainVM.errorMessage.value.isEmpty() == false) {
                        Toast.makeText(context, mainVM.errorMessage.value,
                            Toast.LENGTH_LONG).show()
                    }

                    LazyColumn() {
                        items(items = users) {
                            Column(modifier = Modifier
                                .padding(horizontal = 12.dp)
                                .padding(bottom = 20.dp)
                                .clickable {
                                    backStack.add(Routes.UserDetails(mainVM = mainVM,
                                        uid = it.id ?: 1))
                                }) {
                                Card(modifier = Modifier
                                    .padding(horizontal = 10.dp)
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
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.fillMaxWidth())
                                        Text("Gender: ${it.gender}",
                                            textAlign = TextAlign.Center,
                                            fontSize = 18.sp,
                                            modifier = Modifier.fillMaxWidth())
                                        Text("Status: ${it.status}",
                                            textAlign = TextAlign.Center,
                                            fontSize = 18.sp,
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
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(end = 25.dp)
                        .padding(bottom = 25.dp)
                        .fillMaxSize()) {
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


