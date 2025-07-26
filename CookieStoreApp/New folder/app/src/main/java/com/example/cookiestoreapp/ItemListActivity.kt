package com.example.cookiestoreapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class ItemListActivity : AppCompatActivity() {

    private lateinit var cookieList: List<Cookie>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)

        val toolbar = findViewById<Toolbar>(R.id.myToolbar)
        setSupportActionBar(toolbar)

        toolbar.setOnMenuItemClickListener { onOptionsItemSelected(it) }

        // Cookie list
        cookieList = listOf(
            Cookie("Chocolate Cookie", 1.99, R.drawable.chocolate, listOf("Milk", "Dark"), 10),
            Cookie("Gingerbread", 2.49, R.drawable.gingerbread, emptyList(), 5),
            Cookie("Oatmeal Raisin", 1.49, R.drawable.oatmeal, listOf("With Nuts", "Without Nuts"), 8)
        )

        val listView = findViewById<ListView>(R.id.listViewCookies)
        val adapter = CookieAdapter(this, cookieList)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedCookie = cookieList[position]
            val intent = Intent(this, PurchaseActivity::class.java)
            intent.putExtra("cookieName", selectedCookie.name)
            intent.putExtra("cookiePrice", selectedCookie.price)
            intent.putExtra("cookieImage", selectedCookie.imageResId)
            intent.putExtra("cookieVariants", ArrayList(selectedCookie.variants))
            intent.putExtra("cookieStock", selectedCookie.stock)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_cookies -> {
                startActivity(Intent(this, ItemListActivity::class.java))
                finish()
                true
            }
            R.id.menu_history -> {
                startActivity(Intent(this, HistoryActivity::class.java))
                true
            }
            R.id.menu_logout -> {
                DataManager.currentUser = null
                startActivity(Intent(this, MainActivity::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
