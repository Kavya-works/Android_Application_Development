package com.example.quiz_app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val scoreText = findViewById<TextView>(R.id.scoreText)
        val retryButton = findViewById<Button>(R.id.retryButton)
        val exitButton = findViewById<Button>(R.id.exitButton)

        val score = intent.getIntExtra("score", 0)
        scoreText.text = "You scored $score out of 3!"

        showCustomDialog(score)

        retryButton.setOnClickListener {
            QuizState.reset()
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("difficulty", QuizState.difficulty)
            startActivity(intent)
            finish()
        }

        exitButton.setOnClickListener {
            QuizState.reset()
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun showCustomDialog(score: Int) {
        val message = if (score == 3) "Excellent!" else "Good job!"
        AlertDialog.Builder(this)
            .setTitle("Great Job!")
            .setMessage("$message\nYou got $score out of 3 correct.")
            .setPositiveButton("OK", null)
            .show()
    }
}
