package com.example.gorestapi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date

class MainViewModel: ViewModel() {
    var hostUrl = "gorest.co.in"
    val authValue = "Bearer 8d9a9d2a6de3123f4ca1acf6375037746da0923d456bf5d07ca82eee2ea8b02b"
    var httpClient: HttpClient? = null

    private var _users = MutableStateFlow<List<User>>(emptyList())
    val users = _users.asStateFlow()

    private var _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()

    init {
        httpClient = HttpClient(Android) {
            install(ContentNegotiation) {
                json()
            }
        }
        getUsers()
    }

    fun getUser(uid: Int) {
        viewModelScope.launch {
            val response = httpClient?.get("https://gorest.co.in/public/v2/users/${uid}") {
                headers {
                    append(HttpHeaders.Accept, "application/json")
                    append(HttpHeaders.Authorization, "Bearer 8d9a9d2a6de3123f4ca1acf6375037746da0923d456bf5d07ca82eee2ea8b02b")

                }
            }
            _user.value = response?.body<User>()
        }
    }

    fun getUsers() {
        viewModelScope.launch {
            try {
                val fetchedUsers = httpClient?.get {
                    url {
                        contentType(ContentType.Application.Json)
                        header("Authorization", authValue)
                        protocol = URLProtocol.HTTPS
                        host = hostUrl
                        path("/public/v2/users")
                    }
                }?.body<List<User>>() ?: emptyList()
                _users.value = fetchedUsers
            } catch (exc: Exception) {
                println(exc.message)
                println(exc.stackTrace)
            }
        }
    }
}
