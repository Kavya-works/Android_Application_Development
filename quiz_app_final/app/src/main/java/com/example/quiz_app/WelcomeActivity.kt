package com.example.quiz_app

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class
WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val difficultyGroup = findViewById<RadioGroup>(R.id.difficultyGroup)
        val startButton = findViewById<Button>(R.id.startQuizButton)

        startButton.setOnClickListener {
            val selectedId = difficultyGroup.checkedRadioButtonId

            if (selectedId == -1) {
                Toast.makeText(this, "Please select a difficulty.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedRadio = findViewById<RadioButton>(selectedId)
            val difficulty = selectedRadio.text.toString()

            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("difficulty", difficulty)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_welcome, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_about -> {
                showDialog("About", "Quiz App\nCreated by Kavya")
                true
            }
            R.id.menu_help -> {
                showDialog("Help", "Choose a difficulty and press Start Quiz to begin.")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showDialog(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }
}
