package com.example.gorestapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.example.gorestapi.ui.theme.GoRESTAPITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GoRESTAPITheme {
                val backStack = remember { mutableStateListOf<Any>(Routes.UserList) }

                NavDisplay(backStack = backStack,
                    onBack = {backStack.removeLastOrNull()},
                    entryProvider = { key ->
                        when (key) {
                            is Routes.UserList -> NavEntry(key) {
                                UserList(backStack = backStack)
                            }
                            is Routes.UserDetails -> NavEntry(key) {
                                UserDetails(backStack = backStack,
                                    mainVM = key.mainVM,
                                    uid = key.uid)
                            }
                            else -> NavEntry(Unit) {
                                Text("Unknown route!")
                            }
                        }
                    })
            }
        }
    }
}

