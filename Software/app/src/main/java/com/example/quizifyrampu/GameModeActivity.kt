package com.example.quizify

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.quizifyrampu.R

class GameModeActivity : AppCompatActivity() {

    private lateinit var btnClassic: Button
    private lateinit var btnTimeRush: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_mode)

        btnClassic = findViewById(R.id.btnClassic)
        btnTimeRush = findViewById(R.id.btnTimeRush)

        btnClassic.setOnClickListener { startCategorySelection("classic") }
        btnTimeRush.setOnClickListener { startCategorySelection("timeRush") }
    }

    private fun startCategorySelection(gameMode: String) {
        val intent = Intent(this, CategorySelectionActivity::class.java)
        intent.putExtra("gameMode", gameMode)
        startActivity(intent)
    }
}