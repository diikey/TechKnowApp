package com.example.techknowapp.core.rest

data class DynamicResponse(
    var results: List<Map<String, String?>>? = null,
    val id: String,
    val course_code: String,
    val student_id: String,
    val is_approved: Boolean,
    val created_at: String,
)
