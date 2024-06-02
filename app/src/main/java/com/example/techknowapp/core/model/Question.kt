package com.example.techknowapp.core.model

data class Question(
    val answer: String,
    val id: Int,
    val option1: String,
    val option2: String,
    val option3: String,
    val option4: String,
    val question: String
)