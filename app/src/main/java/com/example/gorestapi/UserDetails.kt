package com.example.gorestapi

import android.R.id.message
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ElevatedButton
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
    }) { innerPadding ->
        Column(verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.fillMaxSize()
                .padding(horizontal = 25.dp)
                .padding(vertical = innerPadding.calculateTopPadding())) {
            LabelValueRow("Name: ", user?.name ?: "")
            LabelValueRow("Email: ", user?.email ?: "")
            LabelValueRow("Gender: ", user?.gender ?: "")
            LabelValueRow("Status: ", user?.status ?: "")
            if (user?.id != null) { // Todo: Progress-Indikator while loading.
                Row(horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 15.dp)) {
                    ElevatedButton(onClick = {
                        mainVM.deleteUser(uid = user.id)
                        backStack.removeLastOrNull()
                    }) {
                        Text("Delete")
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}
