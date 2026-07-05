package com.example.gorestapi

object Routes {
    data object UserList
    data class UserDetails(val mainVM: MainViewModel,
                           val uid: Int)
}