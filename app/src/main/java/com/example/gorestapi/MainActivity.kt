package com.example.gorestapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.gorestapi.ui.theme.GoRESTAPITheme
import java.util.Date
import java.util.Objects

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

