package com.example.quizifyrampu

object UserSession_staro {
    private var firstName: String? = null
    private var lastName: String? = null
    var username: String? = null
    var password: String? = null

    fun saveUserSession(firstName: String, lastName: String, username: String, password: String) {
        this.firstName = firstName
        this.lastName = lastName
        this.username = username
        this.password = password
    }
}
