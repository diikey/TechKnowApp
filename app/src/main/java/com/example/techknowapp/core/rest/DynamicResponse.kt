package com.example.techknowapp.core.rest

data class DynamicResponse(
    var results: List<Map<String, String?>>? = null,
)
