package com.example.techknowapp.feature.registration

import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.techknowapp.R
import com.example.techknowapp.databinding.ActivityRegistrationBinding
import com.example.techknowapp.feature.registration.utils.RegistrationApiCallback
import com.example.techknowapp.feature.registration.utils.RegistrationApiUtils
import timber.log.Timber

class RegistrationActivity : AppCompatActivity(), RegistrationApiCallback {
    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var registrationApiUtils: RegistrationApiUtils

    private val onBackCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (binding.pbLoading.visibility == View.GONE) {
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(this, onBackCallback)
        registrationApiUtils = RegistrationApiUtils(applicationContext, this)

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
        binding.ivEyeConfirmation.setOnClickListener {
            if (binding.etPasswordConfirmation.transformationMethod == null) {
                binding.etPasswordConfirmation.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                binding.etPasswordConfirmation.setSelection(binding.etPasswordConfirmation.text.length)
                binding.ivEyeConfirmation.setImageResource(R.mipmap.eye)
            } else {
                binding.etPasswordConfirmation.transformationMethod = null
                binding.etPasswordConfirmation.setSelection(binding.etPasswordConfirmation.text.length)
                binding.ivEyeConfirmation.setImageResource(R.mipmap.hide_eye)
            }
        }
        binding.btnSubmit.setOnClickListener {
            if (!validateFields()) return@setOnClickListener
            hideShowLoading(true)
            val params = HashMap<String, String>().apply {
                put("username", binding.etUsername.text.toString())
                put("password", binding.etPassword.text.toString())
                put("email", binding.etEmail.text.toString())
            }
            registrationApiUtils.register(params)
        }
    }

    private fun validateFields(): Boolean {
        if (binding.etUsername.text.toString().isBlank() ||
            binding.etPassword.text.toString().isBlank() ||
            binding.etEmail.text.toString().isBlank()
        ) {
            Toast.makeText(applicationContext, "Please fill up all fields!", Toast.LENGTH_SHORT)
                .show()
            return false
        }

        if (binding.etPassword.text.toString() != binding.etPasswordConfirmation.text.toString()) {
            Toast.makeText(applicationContext, "Password doesn't match.", Toast.LENGTH_SHORT)
                .show()
            return false
        }

        return true
    }

    private fun hideShowLoading(isShow: Boolean) {
        if (isShow) {
            binding.btnSubmit.visibility = View.GONE
            binding.pbLoading.visibility = View.VISIBLE
            return
        }
        binding.btnSubmit.visibility = View.VISIBLE
        binding.pbLoading.visibility = View.GONE
    }

    override fun registerResult(apiResult: String) {
        hideShowLoading(false)
        when (apiResult) {
            RegistrationApiUtils.API_SUCCESS -> {
                Toast.makeText(applicationContext, "Registration Successful", Toast.LENGTH_SHORT)
                    .show()
                finish()
            }

            RegistrationApiUtils.API_FAILED -> {
                Toast.makeText(
                    applicationContext,
                    "Something went wrong, please try again.",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    }
}