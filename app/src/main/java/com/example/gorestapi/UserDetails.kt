package com.example.gorestapi

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun UserDetails(backStack: SnapshotStateList<Any>, mainVM: MainViewModel, uid: Int) {
    mainVM.getUser(uid = uid)

    var user = mainVM.user.collectAsState().value

    Scaffold(topBar = {
        TopAppBar(title = {
            Text("User details")
        }, navigationIcon = {
            IconButton(onClick = {
                backStack.removeLastOrNull()
            }) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "")
            }
        })
    }) {
        Column(verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.fillMaxSize()
                                .padding(horizontal = 25.dp)
                                .padding(vertical = 20.dp)) {
            Text("Name: ${user?.name ?: ""}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold)
            Text("Email: ${user?.email ?: ""}",
                fontSize = 20.sp)
            Text("Gender: ${user?.gender ?: ""}",
                fontSize = 20.sp)
            Text("Status: ${user?.status ?: ""}",
                fontSize = 20.sp)
        }
    }
}