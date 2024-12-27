package com.example.quizifyrampu

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class GameModeActivity : AppCompatActivity() {

    private lateinit var btnClassic: Button
    private lateinit var btnTimeRush: Button
    private lateinit var btnStatistics: Button
    private lateinit var btnAddQuestion: Button
    private lateinit var profileButton: ImageView
    private lateinit var exitButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_mode)

        // Inicijalizacija UI elemenata
        try {
            btnClassic = findViewById(R.id.btnClassic)
            btnTimeRush = findViewById(R.id.btnTimeRush)
            btnAddQuestion = findViewById(R.id.btnAddQuestion)
            btnStatistics = findViewById(R.id.btnStatistics)
            profileButton = findViewById(R.id.btn_profile)
            exitButton = findViewById(R.id.btn_exit)

            btnClassic.setOnClickListener { startCategorySelection("classic") }
            btnTimeRush.setOnClickListener { startCategorySelection("timeRush") }

            btnAddQuestion.setOnClickListener {
                val intent = Intent(this, AddQuestionActivity::class.java)
                startActivity(intent)
            }

            btnStatistics.setOnClickListener {
                checkStatisticsAndOpenActivity()
            }

            profileButton.setOnClickListener {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
            }

            exitButton.setOnClickListener {
                finishAffinity()
            }

            // Setovanje gradienta na naslov
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

        } catch (e: Exception) {
            Toast.makeText(this, "Greška u inicijalizaciji: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startCategorySelection(gameMode: String) {
        val intent = Intent(this, CategorySelectionActivity::class.java)
        intent.putExtra("gameMode", gameMode)
        startActivity(intent)
    }

    private fun checkStatisticsAndOpenActivity() {
        val userId = getSharedPreferences("user_session", Context.MODE_PRIVATE).getInt("user_id", -1)

        if (userId == -1) {
            Toast.makeText(this, "Niste prijavljeni", Toast.LENGTH_SHORT).show()
            return
        }

        val credentials = Credentials.basic("aplikatori", "nA7:B&")
        val request = Request.Builder()
            .url("http://157.230.8.219/quizify/get_user_statistics.php?user_id=$userId")
            .addHeader("Authorization", credentials)
            .get()
            .build()

        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@GameModeActivity, "Greška: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                if (response.isSuccessful && responseBody != null) {
                    try {
                        val jsonObject = JSONObject(responseBody)
                        val jsonArray = jsonObject.optJSONArray("data")

                        if (jsonArray == null || jsonArray.length() == 0) {
                            runOnUiThread {
                                Toast.makeText(this@GameModeActivity, "Nema dostupne statistike za prikaz.", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            runOnUiThread {
                                val intent = Intent(this@GameModeActivity, StatisticsActivity::class.java)
                                startActivity(intent)
                            }
                        }
                    } catch (e: Exception) {
                        runOnUiThread {
                            Toast.makeText(this@GameModeActivity, "Greška u obradi podataka", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@GameModeActivity, "Greška u dohvatu podataka: HTTP ${response.code}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}
