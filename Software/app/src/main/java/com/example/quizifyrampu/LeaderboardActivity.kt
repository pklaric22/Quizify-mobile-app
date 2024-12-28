package com.example.quizifyrampu

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONArray
import java.io.IOException

class LeaderboardActivity : AppCompatActivity() {

    private lateinit var leaderboardListView: ListView
    private lateinit var backToMenuButton: Button
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)

        leaderboardListView = findViewById(R.id.lv_leaderboard)
        backToMenuButton = findViewById(R.id.btn_back_to_menu)

        fetchLeaderboardData()

        backToMenuButton.setOnClickListener {
            val intent = Intent(this, GameModeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun fetchLeaderboardData() {
        val url = "http://157.230.8.219/quizify/get_leaderboard.php"
        val request = Request.Builder().url(url).get().build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(
                        this@LeaderboardActivity,
                        "Failed to fetch leaderboard data",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                if (!response.isSuccessful || responseBody.isNullOrEmpty()) {
                    runOnUiThread {
                        Toast.makeText(
                            this@LeaderboardActivity,
                            "Failed to fetch leaderboard data",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    return
                }

                try {
                    val jsonArray = JSONArray(responseBody)
                    val leaderboardData = mutableListOf<String>()

                    for (i in 0 until jsonArray.length()) {
                        val player = jsonArray.getJSONObject(i)
                        val name = player.optString("name", "Unknown")
                        val score = player.optInt("score", 0)
                        leaderboardData.add("${i + 1}. $name - $score points")
                    }

                    runOnUiThread {
                        val adapter = ArrayAdapter(
                            this@LeaderboardActivity,
                            android.R.layout.simple_list_item_1,
                            leaderboardData
                        )
                        leaderboardListView.adapter = adapter
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        Toast.makeText(
                            this@LeaderboardActivity,
                            "Error parsing leaderboard data",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        })
    }
}
