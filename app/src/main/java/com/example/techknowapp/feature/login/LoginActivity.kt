package com.example.techknowapp.feature.login

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import androidx.appcompat.app.AppCompatActivity
import com.example.techknowapp.MainActivity
import com.example.techknowapp.R
import com.example.techknowapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            //todo user validation
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}