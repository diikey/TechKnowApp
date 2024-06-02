package com.example.techknowapp.core.model

data class Quiz(
    val course: Int,
    val created_at: String,
    val fill_in_the_blank: Int,
    val id: Int,
    val is_active: Boolean,
    val multiple_choice: Int,
    val name: String,
    val true_false: Int
)