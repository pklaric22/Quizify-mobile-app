package com.example.quizifyrampu

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class LoginActivity_staro : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_staro)

        etUsername = findViewById(R.id.et_login_username)
        etPassword = findViewById(R.id.et_login_password)
        btnLogin = findViewById(R.id.btn_login)

        btnLogin.setOnClickListener { loginUser() }
    }

    private fun loginUser() {
        val username = etUsername.text.toString()
        val password = etPassword.text.toString()

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Molimo ispunite sva polja", Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(this@LoginActivity_staro, "Greška pri prijavi: ${e.message}", Toast.LENGTH_SHORT).show()
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

                            Toast.makeText(this@LoginActivity_staro, "Dobrodošli, $imePrezime", Toast.LENGTH_SHORT).show()

                            val intent = Intent(this@LoginActivity_staro, GameModeActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@LoginActivity_staro, jsonResponse.getString("message"), Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@LoginActivity_staro, "Greška: ${response.code}", Toast.LENGTH_SHORT).show()
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
}