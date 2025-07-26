package com.example.cookiestoreapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.*

class PurchaseActivity : AppCompatActivity() {

    private var cookieStock = 0
    private var price = 0.0
    private var name: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase)

        val toolbar = findViewById<Toolbar>(R.id.myToolbar)
        setSupportActionBar(toolbar)

        toolbar.setOnMenuItemClickListener { onOptionsItemSelected(it) }

        name = intent.getStringExtra("cookieName")
        price = intent.getDoubleExtra("cookiePrice", 0.0)
        cookieStock = intent.getIntExtra("cookieStock", 0)
        val imageResId = intent.getIntExtra("cookieImage", R.drawable.ic_launcher_foreground)
        val variants = intent.getStringArrayListExtra("cookieVariants") ?: arrayListOf()

        val image = findViewById<ImageView>(R.id.imageViewPurchase)
        val nameText = findViewById<TextView>(R.id.textViewCookieName)
        val stockText = findViewById<TextView>(R.id.textViewStock)
        val variantSpinner = findViewById<Spinner>(R.id.spinnerVariant)
        val quantitySpinner = findViewById<Spinner>(R.id.spinnerQuantity)
        val totalText = findViewById<TextView>(R.id.textViewTotal)
        val buyButton = findViewById<Button>(R.id.buttonBuy)

        image.setImageDrawable(ContextCompat.getDrawable(this, imageResId))
        nameText.text = name
        stockText.text = "Stock: $cookieStock"

        if (variants.isNotEmpty()) {
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, variants)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            variantSpinner.adapter = adapter
        } else {
            variantSpinner.visibility = Spinner.GONE
        }

        val quantityOptions = (1..cookieStock).map { it.toString() }
        val qtyAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, quantityOptions)
        qtyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        quantitySpinner.adapter = qtyAdapter

        quantitySpinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                val qty = quantityOptions[position].toInt()
                totalText.text = "Total: $${String.format("%.2f", price * qty)}"
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })

        buyButton.setOnClickListener {
            if (cookieStock <= 0) {
                Toast.makeText(this, "Out of stock!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val qty = quantitySpinner.selectedItem.toString().toInt()
            val variant = if (variantSpinner.visibility == Spinner.VISIBLE) {
                variantSpinner.selectedItem.toString()
            } else {
                null
            }

            val purchase = Purchase(
                cookieName = name ?: "Unknown",
                variant = variant,
                quantity = qty,
                date = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
            )

            val user = DataManager.currentUser
            if (user != null) {
                DataManager.addPurchase(user, purchase)
            }

            AlertDialog.Builder(this)
                .setTitle("Order Confirmed")
                .setMessage("Thanks for buying $qty x ${name ?: "cookie"}!")
                .setPositiveButton("OK") { _, _ -> finish() }
                .show()
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
                startActivity(Intent(this, HistoryActivity::class.java))
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
