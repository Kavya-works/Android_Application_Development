package com.example.quiz_app

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class QuizActivity : AppCompatActivity() {

    private lateinit var questionText: TextView
    private lateinit var optionsGroup: RadioGroup
    private lateinit var hintText: TextView
    private lateinit var hintSwitch: Switch
    private lateinit var nextButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var timerText: TextView
    private var timer: CountDownTimer? = null

    private val questions = listOf(
        Question(
            "What is the capital of France?",
            listOf("Paris", "Rome", "Berlin", "Madrid"),
            "Paris",
            "It’s known for the Eiffel Tower."
        ),
        Question(
            "What is 2 + 2?",
            listOf("3", "4", "5", "22"),
            "4",
            "Basic arithmetic."
        ),
        Question(
            "Which planet is known as the Red Planet?",
            listOf("Earth", "Jupiter", "Mars", "Saturn"),
            "Mars",
            "Named after the Roman god of war."
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        QuizState.reset()
        QuizState.difficulty = intent.getStringExtra("difficulty") ?: "Easy"

        questionText = findViewById(R.id.questionText)
        optionsGroup = findViewById(R.id.optionsGroup)
        hintText = findViewById(R.id.hintText)
        hintSwitch = findViewById(R.id.hintSwitch)
        nextButton = findViewById(R.id.nextButton)
        progressBar = findViewById(R.id.progressBar)
        timerText = findViewById(R.id.timerText)

        supportActionBar?.title = "Question 1"

        loadQuestion()

        hintSwitch.setOnCheckedChangeListener { _, isChecked ->
            hintText.visibility = if (isChecked) TextView.VISIBLE else TextView.GONE
        }

        nextButton.setOnClickListener {
            val selectedId = optionsGroup.checkedRadioButtonId
            if (selectedId == -1) {
                showAlert("Select an answer!")
                return@setOnClickListener
            }

            timer?.cancel()

            val selectedOption = findViewById<RadioButton>(selectedId).text.toString()
            val correctAnswer = questions[QuizState.currentQuestion].correctAnswer

            if (selectedOption == correctAnswer) {
                QuizState.score++
                showAlert("Correct!")
            } else {
                showAlert("Incorrect! The correct answer is $correctAnswer")
            }

            QuizState.currentQuestion++

            if (QuizState.currentQuestion < questions.size) {
                loadQuestion()
            } else {
                goToResult()
            }
        }
    }

    private fun loadQuestion() {
        supportActionBar?.title = "Question ${QuizState.currentQuestion + 1}"
        val q = questions[QuizState.currentQuestion]

        questionText.text = q.question
        hintText.text = q.hint
        hintText.visibility = if (hintSwitch.isChecked) TextView.VISIBLE else TextView.GONE

        optionsGroup.removeAllViews()
        for (option in q.options) {
            val rb = RadioButton(this)
            rb.text = option
            optionsGroup.addView(rb)
        }

        progressBar.progress = ((QuizState.currentQuestion + 1) * 100) / questions.size
        startTimer()
    }

    private fun startTimer() {
        timer?.cancel()
        timer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timerText.text = "Time: ${millisUntilFinished / 1000}"
            }

            override fun onFinish() {
                showAlert("Time’s up!")
                QuizState.currentQuestion++
                if (QuizState.currentQuestion < questions.size) {
                    loadQuestion()
                } else {
                    goToResult()
                }
            }
        }.start()
    }

    private fun showAlert(message: String) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun goToResult() {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("score", QuizState.score)
        startActivity(intent)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_quiz, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_quit -> {
                AlertDialog.Builder(this)
                    .setTitle("Quit Quiz?")
                    .setMessage("Are you sure you want to quit?")
                    .setPositiveButton("Yes") { _, _ ->
                        finish()
                    }
                    .setNegativeButton("No", null)
                    .show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
}

data class Question(
    val question: String,
    val options: List<String>,
    val correctAnswer: String,
    val hint: String
)
