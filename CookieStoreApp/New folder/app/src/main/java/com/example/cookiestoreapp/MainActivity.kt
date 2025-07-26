package com.example.cookiestoreapp

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val users = mapOf(
        "alice" to "wonderland",
        "bob" to "builder",
        "charlie" to "chocolate"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val username = findViewById<EditText>(R.id.editTextUsername)
        val password = findViewById<EditText>(R.id.editTextPassword)
        val loginButton = findViewById<Button>(R.id.buttonLogin)

        loginButton.setOnClickListener {
            val user = username.text.toString().trim()
            val pass = password.text.toString().trim()

            if (users[user] == pass) {
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                DataManager.currentUser = user
                val intent = Intent(this, ItemListActivity::class.java)
                intent.putExtra("username", user)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
