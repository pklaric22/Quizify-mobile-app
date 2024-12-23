package com.example.quizifyrampu

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val playAsGuestButton: Button = findViewById(R.id.btn_guest)
        val registerButton: Button = findViewById(R.id.btn_register)
        val loginButton: Button = findViewById(R.id.btn_sign_in)
        etUsername = findViewById(R.id.et_username_login)
        etPassword = findViewById(R.id.et_password_login)

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

        playAsGuestButton.setOnClickListener {
            clearUserSession()
            val intent = Intent(this, GameModeActivity::class.java)
            startActivity(intent)
        }

        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener { loginUser() }
    }

    private fun loginUser() {
        val username = etUsername.text.toString()
        val password = etPassword.text.toString()

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val json = JSONObject().apply {
            put("username", username)
            put("password", password)
        }

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = json.toString().toRequestBody(mediaType)

        val credentials = Credentials.basic("aplikatori", "nA7:B&")

        val request = Request.Builder()
            .url("http://157.230.8.219/quizify/login_user.php")
            .addHeader("Authorization", credentials)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Login failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                runOnUiThread {
                    if (response.isSuccessful) {
                        val jsonResponse = JSONObject(responseBody ?: "")
                        if (jsonResponse.getString("status") == "success") {
                            val user = jsonResponse.getJSONObject("user")
                            val userId = user.getInt("id")
                            val imePrezime = user.getString("ime_i_prezime")

                            saveUserSession(userId, imePrezime)

                            Toast.makeText(this@MainActivity, "Welcome, $imePrezime", Toast.LENGTH_SHORT).show()

                            val intent = Intent(this@MainActivity, GameModeActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@MainActivity, jsonResponse.getString("message"), Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@MainActivity, "Error: ${response.code}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun saveUserSession(userId: Int, imePrezime: String) {
        val sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("user_id", userId)
        editor.putString("ime_prezime", imePrezime)
        editor.apply()
    }

    private fun clearUserSession() {
        val sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
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
                    Toast.makeText(this@MainActivity, "Greška: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                if (response.isSuccessful && responseBody != null) {
                    try {
                        val jsonObject = JSONObject(responseBody)
                        val jsonArray = jsonObject.optJSONArray("data")

                        if (jsonArray == null || jsonArray.length() == 0) {
                            // Ako nema podataka
                            runOnUiThread {
                                Toast.makeText(this@MainActivity, "Nema dostupne statistike za prikaz.", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            // Ako podaci postoje, otvori aktivnost
                            runOnUiThread {
                                val intent = Intent(this@MainActivity, StatisticsActivity::class.java)
                                startActivity(intent)
                            }
                        }
                    } catch (e: Exception) {
                        runOnUiThread {
                            Toast.makeText(this@MainActivity, "Greška u obradi podataka", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Greška u dohvatu podataka: HTTP ${response.code}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}
