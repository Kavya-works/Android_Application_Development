package com.example.cookiestoreapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat

class CookieAdapter(private val context: Context, private val cookies: List<Cookie>) : BaseAdapter() {

    override fun getCount(): Int = cookies.size
    override fun getItem(position: Int): Any = cookies[position]
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.cookie_item, parent, false)

        val cookie = cookies[position]
        val imageView = view.findViewById<ImageView>(R.id.imageViewCookie)
        val nameText = view.findViewById<TextView>(R.id.textViewCookieName)
        val priceText = view.findViewById<TextView>(R.id.textViewCookiePrice)

        imageView.setImageDrawable(ContextCompat.getDrawable(context, cookie.imageResId))
        nameText.text = cookie.name
        priceText.text = "Price: \$${cookie.price}"

        return view
    }
}
