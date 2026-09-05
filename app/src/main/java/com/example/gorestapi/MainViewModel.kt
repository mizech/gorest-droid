package com.example.gorestapi

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlin.text.isEmpty

class MainViewModel: ViewModel() {
    val authValue = "Bearer 8d9a9d2a6de3123f4ca1acf6375037746da0923d456bf5d07ca82eee2ea8b02b"
    var httpClient: HttpClient? = null

    private var _users = MutableStateFlow<List<User>>(emptyList())
    val users = _users.asStateFlow()

    private var _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()

    var isLoading = mutableStateOf(false)
        private set

    var errorMessage = mutableStateOf("")
        private set

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

    fun validateUserData(userName: String, email: String): Boolean {
        errorMessage.value = ""

        if (userName.isEmpty()) {
            errorMessage.value += "User name mandatory\n"
        }

        if (!Regex(".+@.+\\..+").matches(email)) {
            errorMessage.value += "Missing/Invalid email\n"
        }

        return errorMessage.value.isEmpty()
    }

    // Todo: List isn't updated, when navigating back from details.
    fun addUser(userName: String, email: String, gender: String, status: String) {
        if (!validateUserData(userName = userName, email = email)) {
            return
        }

        viewModelScope.launch {
            errorMessage.value = ""

            try {
                val response = httpClient?.post("https://gorest.co.in/public/v2/users") {
                    headers {
                        append(HttpHeaders.Accept, "application/json")
                        append(HttpHeaders.Authorization, authValue)
                    }

                    contentType(ContentType.Application.Json)

                    setBody(User(name = userName.trim(), email = email.trim(),
                        gender = gender.trim(), status = status.trim()))
                }

                if (response?.status?.value.toString().startsWith("2")) {
                    getUsers()
                } else { // Todo: Display error-messages.
                    val message = "Status code: ${response?.status?.value.toString()}"
                    errorMessage.value = message
                    throw Exception(message)
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
            errorMessage.value = ""

            try {
                val response = httpClient?.get("https://gorest.co.in/public/v2/users/${uid}") {
                    headers {
                        append(HttpHeaders.Accept, "application/json")
                        append(HttpHeaders.Authorization, authValue)
                    }
                }
                _user.value = response?.body<User>()
            } catch (exc: Exception) {
                val message = exc.message
                errorMessage.value = message ?: ""
                println(message ?: "")
            }
        }
    }

    fun getUsers() {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = ""

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
                val message = exc.message
                errorMessage.value = message ?: ""

                println(message ?: "")
                println(" ---------- ")
                println(exc.stackTrace)
            } finally {
                isLoading.value = false
            }
        }
    }

    fun deleteUser(uid: Int) {
        viewModelScope.launch {
            errorMessage.value = ""

            val urlStr = "https://gorest.co.in/public/v2/users/${uid}"
            try {
                httpClient?.delete(urlString = urlStr) {
                    headers {
                        append(HttpHeaders.Authorization, authValue)
                        append(HttpHeaders.Accept, "application/json")
                    }
                }
            } catch (exc: Exception) {
                val message = exc.message
                errorMessage.value = message ?: ""
                println(message ?: "")
            }

            getUsers()
        }
    }

    fun updateUser(uid: Int, userName: String, email: String, gender: String, status: String) {
        if (!validateUserData(userName = userName, email = email)) {
            return
        }

        viewModelScope.launch {
            errorMessage.value = ""

            try {
                httpClient?.put("https://gorest.co.in/public/v2/users/${uid}") {
                    headers {
                        append(HttpHeaders.Authorization, authValue)
                        append(HttpHeaders.Accept, "application/json")
                    }
                    
                    val user = User(id = uid, name = userName, email = email,
                        gender = gender, status = status)
                    setBody(user)
                    contentType(ContentType.Application.Json)

                    getUser(uid = uid)
                }
            } catch (exc: Exception) {
                val message = exc.message
                errorMessage.value = message ?: ""
                println(message)
            }
        }
    }
}

