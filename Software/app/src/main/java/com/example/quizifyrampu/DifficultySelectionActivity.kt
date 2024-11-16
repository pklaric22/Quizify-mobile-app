package com.example.quizify

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.quizifyrampu.R

class DifficultySelectionActivity : AppCompatActivity() {

    private lateinit var tvSelectedGameMode: TextView
    private lateinit var tvSelectedCategory: TextView
    private lateinit var btnEasy: Button
    private lateinit var btnMedium: Button
    private lateinit var btnHard: Button
    private lateinit var btnRandom: Button

    private var selectedGameMode: String? = null
    private var category: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_difficulty_selection)

        tvSelectedGameMode = findViewById(R.id.tvSelectedGameMode)
        tvSelectedCategory = findViewById(R.id.tvSelectedCategory)
        btnEasy = findViewById(R.id.btn_easy)
        btnMedium = findViewById(R.id.btn_medium)
        btnHard = findViewById(R.id.btn_hard)
        btnRandom = findViewById(R.id.btn_random)

        selectedGameMode = intent.getStringExtra("gameMode")
        category = intent.getStringExtra("category")

        tvSelectedGameMode.text = "Game Mode: $selectedGameMode"
        tvSelectedCategory.text = "Kategorija: $category"

        btnEasy.setOnClickListener { startQuiz("easy") }
        btnMedium.setOnClickListener { startQuiz("medium") }
        btnHard.setOnClickListener { startQuiz("hard") }
        btnRandom.setOnClickListener { startQuiz("random") }
    }

    private fun startQuiz(difficulty: String) {
        val intent = Intent(this, if (selectedGameMode == "timeRush") {
            TimeRushActivity::class.java
        } else {
            QuizActivity::class.java
        })

        intent.putExtra("gameMode", selectedGameMode)
        intent.putExtra("category", category)
        intent.putExtra("difficulty", difficulty)

        startActivity(intent)
    }
}