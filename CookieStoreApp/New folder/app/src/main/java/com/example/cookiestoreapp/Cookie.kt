package com.example.cookiestoreapp

data class Cookie(
    val name: String,
    val price: Double,
    val imageResId: Int,
    val variants: List<String>,
    var stock: Int
)
