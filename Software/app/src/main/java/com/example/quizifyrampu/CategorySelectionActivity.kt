package com.example.quizifyrampu

import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CategorySelectionActivity : AppCompatActivity() {

    private lateinit var btnCategorySelection: Button
    private lateinit var btnMovies: Button
    private lateinit var btnGames: Button
    private lateinit var btnHistory: Button
    private lateinit var btnGeography: Button
    private lateinit var btnMusic: Button
    private lateinit var btnPhysics: Button
    private lateinit var btnBiology: Button
    private lateinit var btnCars: Button
    private lateinit var btnSport: Button
    private lateinit var btnIT: Button

    private var gameMode: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_selection)

        gameMode = intent.getStringExtra("gameMode")

        btnCategorySelection = findViewById(R.id.button_all_categories)
        btnMovies = findViewById(R.id.button_movies)
        btnGames = findViewById(R.id.button_games)
        btnHistory = findViewById(R.id.button_history)
        btnGeography = findViewById(R.id.button_geography)
        btnMusic = findViewById(R.id.button_music)
        btnPhysics = findViewById(R.id.button_physics)
        btnBiology = findViewById(R.id.button_biology)
        btnCars = findViewById(R.id.button_cars)
        btnSport = findViewById(R.id.button_sport)
        btnIT = findViewById(R.id.button_it)

        btnCategorySelection.setOnClickListener { startDifficultySelection("General") }
        btnMovies.setOnClickListener { startDifficultySelection("Movies") }
        btnGames.setOnClickListener { startDifficultySelection("Games") }
        btnHistory.setOnClickListener { startDifficultySelection("History") }
        btnGeography.setOnClickListener { startDifficultySelection("Geography") }
        btnMusic.setOnClickListener { startDifficultySelection("Music") }
        btnPhysics.setOnClickListener { startDifficultySelection("Physics") }
        btnBiology.setOnClickListener { startDifficultySelection("Biology") }
        btnCars.setOnClickListener { startDifficultySelection("Cars") }
        btnSport.setOnClickListener { startDifficultySelection("Sport") }
        btnIT.setOnClickListener { startDifficultySelection("IT") }

        val textView = findViewById<TextView>(R.id.tv_app_title)
        val shader = LinearGradient(
            0f, 0f, 0f, textView.textSize * 1.5f,
            intArrayOf(
                Color.parseColor("#00FFFF"),
                Color.parseColor("#4B0082"),
                Color.parseColor("#FFFF00")
            ),
            null,
            Shader.TileMode.CLAMP
        )
        textView.paint.shader = shader
        textView.setShadowLayer(8f, 4f, 4f, Color.YELLOW)
    }

    private fun startDifficultySelection(category: String) {
        val intent = Intent(this, DifficultySelectionActivity::class.java)
        intent.putExtra("gameMode", gameMode)
        intent.putExtra("category", category)
        startActivity(intent)
    }
}