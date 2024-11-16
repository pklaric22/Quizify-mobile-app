package com.example.quizifyrampu

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.quizifyrampu.GameModeActivity
import com.example.quizifyrampu.LoginActivity
import com.example.quizifyrampu.R
import com.example.quizifyrampu.RegisterActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val playAsGuestButton: Button = findViewById(R.id.btn_play_as_guest)
        val registerButton: Button = findViewById(R.id.btn_register)
        val loginButton: Button = findViewById(R.id.btn_login)

        playAsGuestButton.setOnClickListener {
            val intent = Intent(this, GameModeActivity::class.java)
            startActivity(intent)
        }

        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
