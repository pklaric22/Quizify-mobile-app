package com.example.quizifyrampu

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var etFirstName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etFirstName = findViewById(R.id.et_first_name)
        etLastName = findViewById(R.id.et_last_name)
        etUsername = findViewById(R.id.et_username)
        etPassword = findViewById(R.id.et_password)

        findViewById<View>(R.id.btn_register).setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val firstName = etFirstName.text.toString()
        val lastName = etLastName.text.toString()
        val username = etUsername.text.toString()
        val password = etPassword.text.toString()

        if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Molimo ispunite sva polja", Toast.LENGTH_SHORT).show()
        } else {
            val sharedPreferences = getSharedPreferences("QuizifyPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("username", username)
            editor.putString("password", password)
            editor.apply()

            UserSession.saveUserSession(firstName, lastName, username, password)

            Toast.makeText(this, "Registracija uspješna", Toast.LENGTH_SHORT).show()

            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
