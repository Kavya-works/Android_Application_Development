package com.example.cookiestoreapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class HistoryActivity : AppCompatActivity() {

    private lateinit var adapter: ArrayAdapter<Purchase>
    private var history: List<Purchase> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val user = DataManager.currentUser
        if (user == null) {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val toolbar = findViewById<Toolbar>(R.id.myToolbar)
        setSupportActionBar(toolbar)

        val listView = findViewById<ListView>(R.id.listViewHistory)
        val editTextFilter = findViewById<EditText>(R.id.editTextFilter)
        val clearButton = findViewById<Button>(R.id.clearButton)

        history = DataManager.getUserHistory(user)

        adapter = object : ArrayAdapter<Purchase>(
            this,
            R.layout.purchase_item,
            R.id.textViewHistoryName,
            history
        ) {
            override fun getView(position: Int, convertView: android.view.View?, parent: android.view.ViewGroup): android.view.View {
                val view = super.getView(position, convertView, parent)
                val purchase = getItem(position)

                val nameView = view.findViewById<TextView>(R.id.textViewHistoryName)
                val detailsView = view.findViewById<TextView>(R.id.textViewHistoryDetails)

                nameView.text = purchase?.cookieName ?: "Unknown"
                detailsView.text = "Variant: ${purchase?.variant ?: "None"} | Qty: ${purchase?.quantity} | ${purchase?.date}"

                return view
            }
        }

        listView.adapter = adapter

        // Filter logic
        editTextFilter.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val filtered = history.filter {
                    it.cookieName.contains(s.toString(), ignoreCase = true)
                }
                adapter.clear()
                adapter.addAll(filtered)
                adapter.notifyDataSetChanged()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        })

        // Clear button logic
        clearButton.setOnClickListener {
            DataManager.clearUserHistory(user)
            history = emptyList()
            adapter.clear()
            adapter.notifyDataSetChanged()
            Toast.makeText(this, "History cleared", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_cookies -> {
                startActivity(Intent(this, ItemListActivity::class.java))
                finish()
            }
            R.id.menu_history -> {
                Toast.makeText(this, "Already viewing history", Toast.LENGTH_SHORT).show()
            }
            R.id.menu_logout -> {
                DataManager.currentUser = null
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
        return true
    }
}
