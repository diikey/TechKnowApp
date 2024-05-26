package com.example.techknowapp.core.model

data class LoginResponse(
    val token: String,
    val user: User
)