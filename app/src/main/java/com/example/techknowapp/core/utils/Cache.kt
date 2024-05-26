package com.example.techknowapp.core.utils

import android.content.Context
import android.content.SharedPreferences

class Cache(context: Context) {
    private val app: SharedPreferences =
        context.getSharedPreferences("TechKnowApp", Context.MODE_PRIVATE)

    /**
     * SAVE FUNCTIONS
     */
    fun save(key: String?, value: String?) {
        if (value == null) return
        app.edit().putString(key, value).apply()
    }

    fun save(key: String?, value: Boolean?) {
        if (value == null) return
        app.edit().putBoolean(key, value).apply()
    }

    /**
     * GET FUNCTIONS
     */
    fun getString(key: String?, defValue: String = ""): String? {
        return app.getString(key, defValue)
    }

    fun getBoolean(key: String?, defValue: Boolean = false): Boolean {
        return app.getBoolean(key, defValue)
    }

    /**
     * OTHER FUNCTIONS
     */
    fun clear() {
        app.edit().clear().apply()
    }


    fun remove(key: String?) {
        app.edit().remove(key).apply()
    }

    companion object {
        const val TOKEN = "token"
        const val USER_INFO = "user_info"
    }
}