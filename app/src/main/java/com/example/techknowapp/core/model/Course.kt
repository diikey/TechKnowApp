package com.example.techknowapp.core.model

data class Course(
    val id: String,
    val name: String,
    val description: String,
    val image: String?,
    val created_at: String,
    val updated_at: String,
    val is_active: Boolean,
    val course_code: String,
    val created_by: String,
    val updated_by: String,
)
