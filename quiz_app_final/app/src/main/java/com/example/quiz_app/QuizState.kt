package com.example.quiz_app

object QuizState {
    var score = 0
    var currentQuestion = 0
    var difficulty = "Easy"

    fun reset() {
        score = 0
        currentQuestion = 0
    }
}
