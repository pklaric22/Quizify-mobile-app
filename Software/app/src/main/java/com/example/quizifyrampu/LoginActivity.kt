package com.example.quizifyrampu

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quizifyrampu.GameModeActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var etLoginUsername: EditText
    private lateinit var etLoginPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etLoginUsername = findViewById(R.id.et_login_username)
        etLoginPassword = findViewById(R.id.et_login_password)

        findViewById<View>(R.id.btn_login).setOnClickListener {
            loginUser()
        }

        loadUserSession()
    }

    private fun loadUserSession() {
        val sharedPreferences = getSharedPreferences("QuizifyPrefs", Context.MODE_PRIVATE)
        val savedUsername = sharedPreferences.getString("username", null)
        val savedPassword = sharedPreferences.getString("password", null)

        if (savedUsername != null && savedPassword != null) {
            UserSession.saveUserSession(
                firstName = "",
                lastName = "",
                username = savedUsername,
                password = savedPassword
            )
        }
    }

    private fun loginUser() {
        val enteredUsername = etLoginUsername.text.toString()
        val enteredPassword = etLoginPassword.text.toString()

        if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
            Toast.makeText(this, "Molimo ispunite sva polja", Toast.LENGTH_SHORT).show()
            return
        }

        if (enteredUsername == UserSession.username && enteredPassword == UserSession.password) {
            Toast.makeText(this, "Prijava uspješna", Toast.LENGTH_SHORT).show()

            val intent = Intent(this@LoginActivity, GameModeActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Neispravno korisničko ime ili lozinka", Toast.LENGTH_SHORT).show()
        }
    }

}