package com.example.gorestapi

import android.R.bool
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.headers
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.time.LocalDate
import java.util.Objects


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
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }
        getUsers()
    }

    fun addUser(user: String, email: String, gender: String, status: String) {
        viewModelScope.launch {
            try {
                val response = httpClient?.post("https://gorest.co.in/public/v2/users") {
                    headers {
                        append(HttpHeaders.Accept, "application/json")
                        append(HttpHeaders.Authorization, authValue)
                    }

                    contentType(ContentType.Application.Json)

                    setBody(User(name = user.trim(), email = email.trim(),
                        gender = gender.trim(), status = status.trim()))
                }

                if (response?.status?.value.toString().startsWith("2")) {
                    getUsers()
                } else {
                    throw Exception("Status code unequal 200.")
                }
            } catch (exc: Exception) {
                println(exc.message)
                println(" ---------- ")
                println(exc.stackTrace)
            }
        }
    }

    fun getUser(uid: Int) {
        viewModelScope.launch {
            try {
                val response = httpClient?.get("https://gorest.co.in/public/v2/users/${uid}") {
                    headers {
                        append(HttpHeaders.Accept, "application/json")
                        append(HttpHeaders.Authorization, authValue)
                    }
                }
                _user.value = response?.body<User>()
            } catch (exc: Exception) {
                println(exc.message)
                println(" ---------- ")
                println(exc.stackTrace)
            }
        }
    }

    fun getUsers() {
        viewModelScope.launch {
            try {
                val response = httpClient?.get("https://gorest.co.in/public/v2/users/") {
                    headers {
                        append(HttpHeaders.Accept, "application/json")
                        append(HttpHeaders.Authorization, authValue)
                    }
                }

                if (response != null && response.status.value.toString().substring(0, 1) == "2") {
                    _users.value = response.body<List<User>>() ?: emptyList()
                } else {
                    println("Response error-code: ${response?.status?.value.toString()}")
                }
            } catch (exc: Exception) {
                println(exc.message)
                println(" ---------- ")
                println(exc.stackTrace)
            }
        }
    }

    fun deleteUser(uid: Int) {
        viewModelScope.launch {
            val urlStr = "https://gorest.co.in/public/v2/users/${uid}"
            try {
                httpClient?.delete(urlString = urlStr) {
                    headers {
                        append(HttpHeaders.Authorization, "Bearer 8d9a9d2a6de3123f4ca1acf6375037746da0923d456bf5d07ca82eee2ea8b02b")
                        append(HttpHeaders.Accept, "application/json")
                    }
                }
            } catch (exp: Exception) {
                println(exp.message)
            }

            getUsers()
        }
    }
}
