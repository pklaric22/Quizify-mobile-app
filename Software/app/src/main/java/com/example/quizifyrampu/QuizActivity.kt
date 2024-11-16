package com.example.quizifyrampu

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class QuizActivity : AppCompatActivity() {

    private lateinit var tvScore: TextView
    private lateinit var tvQuestionCounter: TextView
    private lateinit var tvQuestion: TextView
    private lateinit var btnAnswer1: Button
    private lateinit var btnAnswer2: Button
    private lateinit var btnAnswer3: Button
    private lateinit var btnAnswer4: Button

    private val client = OkHttpClient()
    private var questions: List<Question> = emptyList()
    private var currentQuestionIndex = 0
    private var score = 0
    private val totalQuestions = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        tvScore = findViewById(R.id.tv_score)
        tvQuestionCounter = findViewById(R.id.tv_question_counter)
        tvQuestion = findViewById(R.id.tv_question)
        btnAnswer1 = findViewById(R.id.btn_answer1)
        btnAnswer2 = findViewById(R.id.btn_answer2)
        btnAnswer3 = findViewById(R.id.btn_answer3)
        btnAnswer4 = findViewById(R.id.btn_answer4)

        val category = intent.getStringExtra("category") ?: "9"
        val difficulty = intent.getStringExtra("difficulty") ?: "easy"
        val gameMode = intent.getStringExtra("gameMode") ?: "normal"
        Log.d("QuizActivity", "Game Mode: $gameMode, Category: $category, Difficulty: $difficulty")

        fetchQuestions(category, difficulty)
        setButtonClickListeners()

        updateScoreView()
        updateQuestionCounterView()
    }

    private fun setButtonClickListeners() {
        btnAnswer1.setOnClickListener { checkAnswer(btnAnswer1.text.toString()) }
        btnAnswer2.setOnClickListener { checkAnswer(btnAnswer2.text.toString()) }
        btnAnswer3.setOnClickListener { checkAnswer(btnAnswer3.text.toString()) }
        btnAnswer4.setOnClickListener { checkAnswer(btnAnswer4.text.toString()) }
    }

    private fun fetchQuestions(category: String, difficulty: String) {
        val categoryId = when (category) {
            "Movies" -> "11"
            "Games" -> "15"
            "History" -> "23"
            "Geography" -> "22"
            "Music" -> "12"
            "Physics" -> "17"
            "Biology" -> "17"
            "General Knowledge" -> "9"
            "Sports" -> "21"
            "IT" -> "18"
            "Cars" -> "28"
            "All Categories" -> "9"
            else -> "9"
        }

        val url = "https://opentdb.com/api.php?amount=10&category=$categoryId&difficulty=$difficulty&type=multiple"
        Log.d("QuizActivity", "Dohvaćanje pitanja sa linka: $url")

        val request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("QuizActivity", "Neuspjelo dohvaćanje pitanja", e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    Log.e("QuizActivity", "Odgovor neuspješan: ${response.code}")
                    return
                }

                response.body?.let { responseBody ->
                    val json = JSONObject(responseBody.string())
                    val results = json.getJSONArray("results")
                    val questionList = mutableListOf<Question>()

                    for (i in 0 until results.length()) {
                        val questionObj = results.getJSONObject(i)
                        val question = decodeHtml(questionObj.getString("question"))
                        val correctAnswer = decodeHtml(questionObj.getString("correct_answer"))
                        val incorrectAnswers = questionObj.getJSONArray("incorrect_answers")
                        val answers = mutableListOf(correctAnswer)

                        for (j in 0 until incorrectAnswers.length()) {
                            answers.add(decodeHtml(incorrectAnswers.getString(j)))
                        }
                        answers.shuffle()

                        questionList.add(Question(question, correctAnswer, answers))
                    }

                    questions = questionList
                    currentQuestionIndex = 0

                    Log.d("QuizActivity", "Pitanje dohvaćeno: ${questions.size}")

                    runOnUiThread {
                        if (questions.isNotEmpty()) {
                            updateUI(questions[currentQuestionIndex])
                        } else {
                            Log.e("QuizActivity", "Pitanja nisu dohvaćena!")
                        }
                    }
                } ?: Log.e("QuizActivity", "Odgovor u body je prazan")
            }
        })
    }

    private fun updateUI(question: Question) {
        tvQuestion.text = question.text
        btnAnswer1.text = question.answers[0]
        btnAnswer2.text = question.answers[1]
        btnAnswer3.text = question.answers[2]
        btnAnswer4.text = question.answers[3]
        setButtonsEnabled(true)
    }

    private fun checkAnswer(selectedAnswer: String) {
        setButtonsEnabled(false)
        resetButtonColors()

        val correctAnswer = questions[currentQuestionIndex].correctAnswer
        val difficulty = intent.getStringExtra("difficulty") ?: "easy"

        if (selectedAnswer == correctAnswer) {
            Log.d("QuizActivity", "Točan odgovor: $selectedAnswer")
            highlightCorrectAnswer(selectedAnswer)
            score += getScoreForDifficulty(difficulty)
            updateScoreView()
        } else {
            Log.d("QuizActivity", "Netočan odgovor: $selectedAnswer")
            highlightIncorrectAnswer(selectedAnswer)
        }

        Handler(Looper.getMainLooper()).postDelayed({
            resetButtonColors()

            if (currentQuestionIndex < questions.size - 1) {
                currentQuestionIndex++
                updateUI(questions[currentQuestionIndex])
                updateQuestionCounterView()
            } else {
                finishQuiz()
            }
        }, 1000)
    }

    private fun getScoreForDifficulty(difficulty: String): Int {
        return when (difficulty) {
            "easy" -> 100
            "medium" -> 500
            "hard" -> 1000
            else -> 100
        }
    }

    private fun setButtonsEnabled(enabled: Boolean) {
        btnAnswer1.isEnabled = enabled
        btnAnswer2.isEnabled = enabled
        btnAnswer3.isEnabled = enabled
        btnAnswer4.isEnabled = enabled
    }

    private fun highlightCorrectAnswer(answer: String) {
        when (answer) {
            btnAnswer1.text -> btnAnswer1.setBackgroundColor(getColor(android.R.color.holo_green_light))
            btnAnswer2.text -> btnAnswer2.setBackgroundColor(getColor(android.R.color.holo_green_light))
            btnAnswer3.text -> btnAnswer3.setBackgroundColor(getColor(android.R.color.holo_green_light))
            btnAnswer4.text -> btnAnswer4.setBackgroundColor(getColor(android.R.color.holo_green_light))
        }
    }

    private fun highlightIncorrectAnswer(answer: String) {
        when (answer) {
            btnAnswer1.text -> btnAnswer1.setBackgroundColor(getColor(android.R.color.holo_red_light))
            btnAnswer2.text -> btnAnswer2.setBackgroundColor(getColor(android.R.color.holo_red_light))
            btnAnswer3.text -> btnAnswer3.setBackgroundColor(getColor(android.R.color.holo_red_light))
            btnAnswer4.text -> btnAnswer4.setBackgroundColor(getColor(android.R.color.holo_red_light))
        }
        highlightCorrectAnswer(questions[currentQuestionIndex].correctAnswer)
    }

    private fun resetButtonColors() {
        btnAnswer1.setBackgroundColor(getColor(android.R.color.darker_gray))
        btnAnswer2.setBackgroundColor(getColor(android.R.color.darker_gray))
        btnAnswer3.setBackgroundColor(getColor(android.R.color.darker_gray))
        btnAnswer4.setBackgroundColor(getColor(android.R.color.darker_gray))
    }

    private fun decodeHtml(html: String): String {
        return html.replace("&quot;", "\"")
            .replace("&amp;", "&")
            .replace("&lt;", "<")
            .replace("&gt;", ">")
            .replace("&uuml;", "ü")
            .replace("&Ouml;", "Ö")
            .replace("&#039;", "'")
            .replace("&eacute;", "é")
            .replace("&Eacute;", "É")
            .replace("&agrave;", "à")
            .replace("&Agrave;", "À")
            .replace("&aacute;", "á")
            .replace("&Aacute;", "Á")
            .replace("&acirc;", "â")
            .replace("&Acirc;", "Â")
            .replace("&atilde;", "ã")
            .replace("&Atilde;", "Ã")
            .replace("&auml;", "ä")
            .replace("&Auml;", "Ä")
            .replace("&aring;", "å")
            .replace("&Aring;", "Å")
            .replace("&ccedil;", "ç")
            .replace("&Ccedil;", "Ç")
            .replace("&egrave;", "è")
            .replace("&Egrave;", "È")
            .replace("&euml;", "ë")
            .replace("&Euml;", "Ë")
            .replace("&iacute;", "í")
            .replace("&Iacute;", "Í")
            .replace("&icirc;", "î")
            .replace("&Icirc;", "Î")
            .replace("&iuml;", "ï")
            .replace("&Iuml;", "Ï")
            .replace("&oacute;", "ó")
            .replace("&Oacute;", "Ó")
            .replace("&ocirc;", "ô")
            .replace("&Ocirc;", "Ô")
            .replace("&otilde;", "õ")
            .replace("&Otilde;", "Õ")
            .replace("&ouml;", "ö")
            .replace("&Ouml;", "Ö")
            .replace("&uacute;", "ú")
            .replace("&Uacute;", "Ú")
            .replace("&ucirc;", "û")
            .replace("&Ucirc;", "Û")
            .replace("&uuml;", "ü")
            .replace("&Uuml;", "Ü")
            .replace("&yacute;", "ý")
            .replace("&Yacute;", "Ý")
            .replace("&yuml;", "ÿ")
            .replace("&Yuml;", "Ÿ")
            .replace("&ntilde;", "ñ")
            .replace("&Ntilde;", "Ñ")
            .replace("&szlig;", "ß")
            .replace("&euro;", "€")
            .replace("&pound;", "£")
            .replace("&yen;", "¥")
            .replace("&copy;", "©")
            .replace("&reg;", "®")
            .replace("&trade;", "™")
            .replace("&deg;", "°")
            .replace("&laquo;", "«")
            .replace("&raquo;", "»")
            .replace("&laquo;", "«")
            .replace("&bull;", "•")
            .replace("&prime;", "′")
            .replace("&Prime;", "″")
            .replace("&lsquo;", "‘")
            .replace("&rsquo;", "’")
            .replace("&sbquo;", "‚")
            .replace("&ldquo;", "“")
            .replace("&rdquo;", "”")
            .replace("&bdquo;", "„")
            .replace("&ndash;", "–")
            .replace("&mdash;", "—")
            .replace("&lsquor;", "‘")
            .replace("&rsquor;", "’")
            .replace("&#8211;", "–")
            .replace("&#8212;", "—")
            .replace("&#8220;", "“")
            .replace("&#8221;", "”")
            .replace("&#8249;", "‹")
            .replace("&#8250;", "›")
            .replace("&#8364;", "€")
            .replace("&#8482;", "™")
            .replace("&#169;", "©")
            .replace("&#174;", "®")
    }

    @SuppressLint("SetTextI18n")
    private fun updateScoreView() {
        tvScore.text = "Bodovi: $score"
    }

    @SuppressLint("SetTextI18n")
    private fun updateQuestionCounterView() {
        tvQuestionCounter.text = "Pitanje ${currentQuestionIndex + 1}/$totalQuestions"
    }

    private fun finishQuiz() {
        Log.d("QuizActivity", "Kviz završen. Finalni bodovi: $score")
        val intent = Intent(this, GameModeActivity::class.java)
        intent.putExtra("finalScore", score)
        startActivity(intent)
        finish()
    }

    data class Question(
        val text: String,
        val correctAnswer: String,
        val answers: List<String>
    )
}