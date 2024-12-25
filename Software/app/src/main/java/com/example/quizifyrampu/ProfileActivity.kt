package com.example.quizifyrampu

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class ProfileActivity : AppCompatActivity() {

    private lateinit var tvFirstName: TextView
    private lateinit var tvUsername: TextView
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnEditProfile: Button

    private val client = OkHttpClient()
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        tvFirstName = findViewById(R.id.tv_first_name_profile)
        tvUsername = findViewById(R.id.tv_username_profile)
        etEmail = findViewById(R.id.et_email_profile)
        etPassword = findViewById(R.id.et_password_profile)
        btnEditProfile = findViewById(R.id.btn_edit_profile)

        val sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE)
        userId = sharedPreferences.getInt("user_id", -1)

        if (userId == -1) {
            Toast.makeText(this, "Niste prijavljeni", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            fetchUserProfile(userId)
        }

        btnEditProfile.setOnClickListener {
            updateUserProfile(userId)
        }

        val btnAddQuestion: Button = findViewById(R.id.btn_add_question)

        btnAddQuestion.setOnClickListener {
            val intent = Intent(this, AddQuestionActivity::class.java)
            startActivity(intent)
        }
    }

    private fun fetchUserProfile(userId: Int) {
        val credentials = Credentials.basic("aplikatori", "nA7:B&")
        val request = Request.Builder()
            .url("http://157.230.8.219/quizify/get_user_profile.php?user_id=$userId")
            .addHeader("Authorization", credentials)
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@ProfileActivity, "Greška pri dohvatu podataka: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                Log.d("ProfileActivity", "HTTP Status Code: ${response.code}")
                Log.d("ProfileActivity", "Odgovor servera: $responseBody")

                if (response.isSuccessful && responseBody != null) {
                    try {
                        val jsonResponse = JSONObject(responseBody)
                        val status = jsonResponse.getString("status")
                        Log.d("ProfileActivity", "Status: $status")

                        if (status == "success") {
                            val data = jsonResponse.getJSONObject("data")
                            Log.d("ProfileActivity", "Dohvaćeni podaci: $data")

                            runOnUiThread {
                                tvFirstName.text = data.optString("ime_i_prezime", "N/A")
                                tvUsername.text = data.optString("username", "N/A")
                                etEmail.setText(data.optString("email", ""))
                            }
                        } else {
                            val message = jsonResponse.optString("message")
                            Log.e("ProfileActivity", "Greška u odgovoru: $message")
                            runOnUiThread {
                                Toast.makeText(this@ProfileActivity, "Greška: $message", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("ProfileActivity", "Greška u parsiranju JSON odgovora: ${e.message}", e)
                        runOnUiThread {
                            Toast.makeText(this@ProfileActivity, "Greška u parsiranju podataka", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Log.e("ProfileActivity", "Neuspješan odgovor: HTTP ${response.code}")
                    runOnUiThread {
                        Toast.makeText(this@ProfileActivity, "Greška u dohvatu podataka", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        })
    }

    private fun updateUserProfile(userId: Int) {
        val credentials = Credentials.basic("aplikatori", "nA7:B&")

        val json = JSONObject().apply {
            put("user_id", userId)
            put("email", etEmail.text.toString())
            put("password", etPassword.text.toString())
        }

        val body = json.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
        val request = Request.Builder()
            .url("http://157.230.8.219/quizify/update_user_profile.php")
            .addHeader("Authorization", credentials)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@ProfileActivity, "Greška pri ažuriranju profila: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                if (response.isSuccessful && responseBody != null) {
                    val jsonResponse = JSONObject(responseBody)
                    val message = jsonResponse.getString("message")
                    runOnUiThread {
                        Toast.makeText(this@ProfileActivity, message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@ProfileActivity, "Greška pri ažuriranju profila", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

}