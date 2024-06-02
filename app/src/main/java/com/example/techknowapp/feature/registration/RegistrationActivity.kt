package com.example.techknowapp.feature.registration

import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
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

        var pass = binding.etPassword.text.toString()
        Timber.d("length " + (pass.length<8) + " pass digit " + (pass.isDigitsOnly()) + " pass letter " + (pass.all { it.isLetter() }) + " ismix lower upper " + !(pass.any(Char::isLowerCase) && pass.any(Char::isUpperCase)))
//        if (pass.length<8 || !pass.isDigitsOnly() || (pass.any(Char::isLowerCase) && pass.any(Char::isUpperCase)) || pass.all { it.isLetter() }){
//            return false
//        }

        if (pass.length<8){
            Toast.makeText(applicationContext, "Password must contain at least 8 characters.", Toast.LENGTH_SHORT)
                .show()
            return false
        }

        if (pass.isDigitsOnly()){
            Toast.makeText(applicationContext, "Password must contain letters and numbers.", Toast.LENGTH_SHORT)
                .show()
            return false
        }

        if (pass.all { it.isLetter() }){
            Toast.makeText(applicationContext, "Password must contain letters and numbers.", Toast.LENGTH_SHORT)
                .show()
            return false
        }

        if (!(pass.any(Char::isLowerCase) && pass.any(Char::isUpperCase))){
            Toast.makeText(applicationContext, "Password must contain lowercase and uppercase.", Toast.LENGTH_SHORT)
                .show()
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