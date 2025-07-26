package com.example.cookiestoreapp

data class Purchase(
    val cookieName: String,
    val variant: String?,
    val quantity: Int,
    val date: String
)

object DataManager {
    var currentUser: String? = null

    val purchaseHistory = mutableMapOf<String, MutableList<Purchase>>()

    fun addPurchase(user: String, purchase: Purchase) {
        val history = purchaseHistory.getOrPut(user) { mutableListOf() }
        history.add(purchase)
    }

    fun getUserHistory(user: String): List<Purchase> {
        return purchaseHistory[user] ?: emptyList()
    }

    fun clearUserHistory(user: String) {
        purchaseHistory[user] = mutableListOf()
    }
}
