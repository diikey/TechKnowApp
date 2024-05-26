package com.example.techknowapp.feature.login

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.techknowapp.MainActivity
import com.example.techknowapp.R
import com.example.techknowapp.core.model.LoginResponse
import com.example.techknowapp.core.utils.Cache
import com.example.techknowapp.databinding.ActivityLoginBinding
import com.example.techknowapp.feature.login.utils.LoginApiCallback
import com.example.techknowapp.feature.login.utils.LoginApiUtils
import com.google.gson.Gson
import timber.log.Timber

class LoginActivity : AppCompatActivity(), LoginApiCallback {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginApiUtils: LoginApiUtils
    private lateinit var cache: Cache

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cache = Cache(applicationContext)
        loginApiUtils = LoginApiUtils(applicationContext, this)

        if (cache.getString(Cache.TOKEN, "") != "") {
            goToMainActivity()
        }

        initComponents()
    }

    private fun initComponents() {
        binding.ivEye.setOnClickListener {
            if (binding.etPassword.transformationMethod == null) {
                binding.etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.etPassword.setSelection(binding.etPassword.text.length)
                binding.ivEye.setImageResource(R.mipmap.eye)
            } else {
                binding.etPassword.transformationMethod = null
                binding.etPassword.setSelection(binding.etPassword.text.length)
                binding.ivEye.setImageResource(R.mipmap.hide_eye)
            }
        }
        binding.btnLogin.setOnClickListener {
            hideShowLoading(true)
            loginApiUtils.login(
                binding.etUsername.text.toString(),
                binding.etPassword.text.toString()
            )
        }
    }

    private fun hideShowLoading(isShow: Boolean) {
        if (isShow) {
            binding.btnLogin.visibility = View.GONE
            binding.pbLoading.visibility = View.VISIBLE
            return
        }
        binding.btnLogin.visibility = View.VISIBLE
        binding.pbLoading.visibility = View.GONE
    }

    private fun goToMainActivity() {
        Intent(this, MainActivity::class.java).apply {
            startActivity(this)
        }
    }

    override fun loginResult(apiResult: String, loginResponse: LoginResponse?) {
        hideShowLoading(false)
        when (apiResult) {
            LoginApiUtils.API_SUCCESS -> {
                cache.save(Cache.TOKEN, loginResponse!!.token)
                cache.save(Cache.USER_INFO, Gson().toJson(loginResponse.user))

                goToMainActivity()
            }

            LoginApiUtils.API_FAILED -> {
                Toast.makeText(
                    applicationContext,
                    "Username/Password is incorrect. Try again.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}