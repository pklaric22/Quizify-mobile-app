package com.example.quizifyrampu

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class AddQuestionActivity : AppCompatActivity() {

    private lateinit var etQuestion: EditText
    private lateinit var etCorrectAnswer: EditText
    private lateinit var etWrongAnswer1: EditText
    private lateinit var etWrongAnswer2: EditText
    private lateinit var etWrongAnswer3: EditText
    private lateinit var spinnerCategory: Spinner
    private lateinit var spinnerDifficulty: Spinner
    private lateinit var btnSaveQuestion: Button

    private val client = OkHttpClient()

    private val categories = listOf(
        "Movies", "Geography", "History", "Games", "Music",
        "Fizics", "Biology", "General", "Sport", "IT"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_question)

        etQuestion = findViewById(R.id.et_question)
        etCorrectAnswer = findViewById(R.id.et_correct_answer)
        etWrongAnswer1 = findViewById(R.id.et_wrong_answer1)
        etWrongAnswer2 = findViewById(R.id.et_wrong_answer2)
        etWrongAnswer3 = findViewById(R.id.et_wrong_answer3)
        spinnerCategory = findViewById(R.id.spinner_category)
        spinnerDifficulty = findViewById(R.id.spinner_difficulty)
        btnSaveQuestion = findViewById(R.id.btn_save_question)

        val difficultyOptions = arrayOf("easy", "medium", "hard")
        val difficultyAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, difficultyOptions)
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDifficulty.adapter = difficultyAdapter

        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = categoryAdapter

        btnSaveQuestion.setOnClickListener {
            saveQuestionToServer()
        }

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

    private fun saveQuestionToServer() {
        val json = Gson().toJson(
            mapOf(
                "text" to etQuestion.text.toString(),
                "difficulty" to spinnerDifficulty.selectedItem.toString(),
                "correct_answer" to etCorrectAnswer.text.toString(),
                "incorrect_answer1" to etWrongAnswer1.text.toString(),
                "incorrect_answer2" to etWrongAnswer2.text.toString(),
                "incorrect_answer3" to etWrongAnswer3.text.toString(),
                "category_id" to getCategoryId(spinnerCategory.selectedItem.toString())
            )
        )

        val credentials = Credentials.basic("aplikatori", "nA7:B&")
        val body = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val request = Request.Builder()
            .url("http://157.230.8.219/quizify/add_question.php")
            .addHeader("Authorization", credentials)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@AddQuestionActivity, "Greška: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AddQuestionActivity, "Pitanje uspješno dodano!", Toast.LENGTH_SHORT).show()
                        clearFields()
                    } else {
                        Toast.makeText(this@AddQuestionActivity, "Greška: ${response.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun getCategoryId(categoryName: String): Int {
        return when (categoryName.uppercase()) {
            "MOVIES" -> 1
            "GEOGRAPHY" -> 2
            "HISTORY" -> 3
            "GAMES" -> 4
            "MUSIC" -> 5
            "FIZICS" -> 6
            "BIOLOGY" -> 7
            "GENERAL" -> 8
            "SPORT" -> 9
            "IT" -> 10
            else -> 0
        }
    }

    private fun clearFields() {
        etQuestion.text.clear()
        etCorrectAnswer.text.clear()
        etWrongAnswer1.text.clear()
        etWrongAnswer2.text.clear()
        etWrongAnswer3.text.clear()
        spinnerCategory.setSelection(0)
        spinnerDifficulty.setSelection(0)
    }
}