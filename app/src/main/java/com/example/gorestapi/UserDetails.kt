package com.example.gorestapi

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LabelValueRow(label: String, value: String) {
    val fontSize = 22.sp

    Row(horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()) {
        Text(label,
            fontWeight = FontWeight.Bold,
            fontSize = fontSize)
        Text(value,
            fontSize = fontSize)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun UserDetails(backStack: SnapshotStateList<Any>, mainVM: MainViewModel, uid: Int) {
    mainVM.getUser(uid = uid)
    var user = mainVM.user.collectAsState().value
    var isDeleteUserDialogShown = remember {
        mutableStateOf(false)
    }
    var isEditUserDialogShown = remember() {
        mutableStateOf(false)
    }

    Scaffold(topBar = {
        TopAppBar(title = {
            Text("User details")
        }, navigationIcon = {
            IconButton(onClick = {
                backStack.removeLastOrNull()
                mainVM.getUsers()
            }) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "")
            }
        })
    }) { innerPadding ->
        if (isEditUserDialogShown.value == false) {
            if (isDeleteUserDialogShown.value == true) {
                BasicAlertDialog(onDismissRequest = {
                    isDeleteUserDialogShown.value = false
                }) {
                    Surface(modifier = Modifier.wrapContentWidth().wrapContentHeight(),
                        shape = MaterialTheme.shapes.large,
                        tonalElevation = AlertDialogDefaults.TonalElevation) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("User becomes deleted. Are you sure?",
                                modifier = Modifier.padding(bottom = 15.dp))
                            Row(modifier = Modifier.fillMaxWidth().padding(),
                                horizontalArrangement = Arrangement.SpaceBetween) {
                                OutlinedButton(onClick = {
                                    if (user?.id != null) {
                                        mainVM.deleteUser(uid = user.id)
                                        backStack.removeLastOrNull()
                                    }
                                }) {
                                    Text("Delete")
                                }
                                OutlinedButton(onClick = {
                                    isDeleteUserDialogShown.value = false
                                }) {
                                    Text("Cancel")
                                }
                            }
                        }
                    }
                }
            }
            Column(verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.fillMaxSize()
                    .padding(horizontal = 25.dp)
                    .padding(vertical = innerPadding.calculateTopPadding())) {
                LabelValueRow("Name: ", user?.name ?: "")
                LabelValueRow("Email: ", user?.email ?: "")
                LabelValueRow("Gender: ", user?.gender ?: "")
                LabelValueRow("Status: ", user?.status ?: "")
                if (user?.id != null) {
                    Row(horizontalArrangement = Arrangement.Absolute.SpaceAround,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 15.dp)) {
                        ElevatedButton(onClick = {
                            isDeleteUserDialogShown.value = true
                        }) {
                            Text("Delete")
                        }
                        ElevatedButton(onClick = {
                            isEditUserDialogShown.value = !isEditUserDialogShown.value
                        }) {
                            Text("Edit")
                        }
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        } else {
            var userGender = Gender.MALE
            val genders = Gender.entries.toTypedArray()
            for (currGender in genders) {
                if (currGender.label == user?.gender) {
                    userGender = currGender
                }
            }

            var userStatus = Status.ACTIVE
            val status = Status.entries.toTypedArray()
            for (currStatus in status) {
                if (currStatus.label == user?.status) {
                    userStatus = currStatus
                }
            }

            UserDialog(isDialogShown = isEditUserDialogShown
                , name = user?.name ?: "", email = user?.email ?: "",
                    gender = userGender, status = userStatus,
                    uid = user?.id, mainVM = mainVM
                ) { name, email, gender, status, uid ->
                uid?.let {
                    mainVM.updateUser(uid = uid, userName = name,
                        email = email, gender = gender,
                        status = status)
                }
            }
        }
    }
}
