package com.example.quizifyrampu

import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class RegisterActivity : AppCompatActivity() {

    private lateinit var etFirstName: EditText
    private lateinit var etUsername: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var spinnerUserType: Spinner
    private lateinit var btnRegister: Button

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etFirstName = findViewById(R.id.et_first_name)
        etUsername = findViewById(R.id.et_username)
        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)
        spinnerUserType = findViewById(R.id.spinner_user_type)
        btnRegister = findViewById(R.id.btn_register)

        val userTypes = listOf("Običan korisnik" to 1, "Admin" to 2)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, userTypes.map { it.first })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerUserType.adapter = adapter

        btnRegister.setOnClickListener { registerUser() }

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

    private fun registerUser() {
        resetInputFieldColors()

        val firstName = etFirstName.text.toString()
        val username = etUsername.text.toString()
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()
        val userTypeId = (spinnerUserType.selectedItemPosition + 1)

        if (!validateInputFields(firstName, username, email, password)) {
            return
        }

        val json = JSONObject().apply {
            put("username", username)
            put("email", email)
            put("password", password)
            put("ime_i_prezime", firstName)
            put("user_type_id", userTypeId)
        }

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = json.toString().toRequestBody(mediaType)

        val credentials = Credentials.basic("aplikatori", "nA7:B&")

        val request = Request.Builder()
            .url("http://157.230.8.219/quizify/register_user.php")
            .addHeader("Authorization", credentials)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@RegisterActivity, "Greška pri registraciji: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                runOnUiThread {
                    Log.d("RegisterActivity", "Odgovor servera: $responseBody")

                    if (response.isSuccessful && responseBody != null) {
                        try {
                            val jsonResponse = JSONObject(responseBody)
                            val status = jsonResponse.getString("status")
                            val message = jsonResponse.getString("message")

                            if (status == "success") {
                                Toast.makeText(this@RegisterActivity, message, Toast.LENGTH_LONG).show()
                                finish()
                            } else {
                                handleRegistrationError(message)
                            }
                        } catch (e: Exception) {
                            Log.e("RegisterActivity", "Greška u obradi odgovora: ${e.message}")
                            Toast.makeText(this@RegisterActivity, "Greška u obradi odgovora", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@RegisterActivity, "Greška: ${response.code}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun validateInputFields(firstName: String, username: String, email: String, password: String): Boolean {
        var isValid = true

        if (firstName.isEmpty()) {
            etFirstName.error = "Obavezno polje"
            etFirstName.setBackgroundColor(Color.RED)
            isValid = false
        }
        if (username.isEmpty()) {
            etUsername.error = "Obavezno polje"
            etUsername.setBackgroundColor(Color.RED)
            isValid = false
        }
        if (email.isEmpty()) {
            etEmail.error = "Obavezno polje"
            etEmail.setBackgroundColor(Color.RED)
            isValid = false
        }
        if (password.isEmpty()) {
            etPassword.error = "Obavezno polje"
            etPassword.setBackgroundColor(Color.RED)
            isValid = false
        }

        if (!isValid) {
            Toast.makeText(this, "Molimo ispunite sva obavezna polja", Toast.LENGTH_SHORT).show()
        }
        return isValid
    }

    private fun resetInputFieldColors() {
        etFirstName.setBackgroundColor(Color.WHITE)
        etUsername.setBackgroundColor(Color.WHITE)
        etEmail.setBackgroundColor(Color.WHITE)
        etPassword.setBackgroundColor(Color.WHITE)
    }

    private fun handleRegistrationError(message: String) {
        when {
            message.contains("Korisničko ime", ignoreCase = true) -> {
                etUsername.error = "Korisničko ime već postoji"
                etUsername.setBackgroundColor(Color.RED)
                Toast.makeText(this, "Korisničko ime već postoji. Odaberite drugo korisničko ime.", Toast.LENGTH_LONG).show()
            }
            message.contains("Email", ignoreCase = true) -> {
                etEmail.error = "Email adresa već postoji"
                etEmail.setBackgroundColor(Color.RED)
                Toast.makeText(this, "Email adresa već postoji. Pokušajte s drugom adresom.", Toast.LENGTH_LONG).show()
            }
            else -> {
                Toast.makeText(this, "Greška pri registraciji: $message", Toast.LENGTH_LONG).show()
            }
        }
    }
}