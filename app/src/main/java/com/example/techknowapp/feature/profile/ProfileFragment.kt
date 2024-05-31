package com.example.techknowapp.feature.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.techknowapp.R
import com.example.techknowapp.core.model.User
import com.example.techknowapp.databinding.FragmentProfileBinding
import com.example.techknowapp.feature.dashboard.utils.DashboardApiUtils
import com.example.techknowapp.feature.profile.utils.ProfileApiCallback
import com.example.techknowapp.feature.profile.utils.ProfileApiUtils
import com.google.gson.Gson
import timber.log.Timber


class ProfileFragment : Fragment(), ProfileApiCallback {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var profileApiUtils: ProfileApiUtils

    private var isEdit = false
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        profileApiUtils = ProfileApiUtils(requireContext(), this)
        initViews()
        initOnClicks()
    }

    private fun initViews() {
        profileApiUtils.getProfile()


        hideShow()
    }

    private fun hideShow() {
        if (isEdit) {
            binding.etBirthday.isEnabled = true
            binding.etGender.isEnabled = true
            binding.etFirstName.isEnabled = true
            binding.etLastName.isEnabled = true
            binding.etStudentID.isEnabled = true
            binding.etEmail.isEnabled = true
            binding.btnSave.visibility = View.VISIBLE
            binding.btnCancel.visibility = View.VISIBLE
            binding.btnEdit.visibility = View.GONE
        } else {
            binding.etBirthday.isEnabled = false
            binding.etGender.isEnabled = false
            binding.etFirstName.isEnabled = false
            binding.etLastName.isEnabled = false
            binding.etStudentID.isEnabled = false
            binding.etEmail.isEnabled = false
            binding.btnSave.visibility = View.GONE
            binding.btnCancel.visibility = View.GONE
            binding.btnEdit.visibility = View.VISIBLE
        }
    }

    private fun initOnClicks() {
        binding.btnEdit.setOnClickListener {
            isEdit = !isEdit
            hideShow()
        }
        binding.btnCancel.setOnClickListener {
            isEdit = false
            hideShow()
        }
        binding.btnSave.setOnClickListener {
            submitProfile()
        }
    }

    private fun checkFields(): Boolean {
        var bool = true
        if (binding.etFirstName.text.toString() == "" || binding.etLastName.text.toString() == ""
            || binding.etGender.text.toString() == "" || binding.etStudentID.text.toString() == ""
            || binding.etBirthday.text.toString() == "" || binding.etEmail.text.toString() == ""
        ) {
            bool = false
        }
        return bool
    }


    private fun submitProfile() {
        if (checkFields()){
            var params = HashMap<String,String>()
            params["username"] = user.username
            params["password"] = user.password
            params["first_name"] = binding.etFirstName.text.toString()
            params["last_name"] = binding.etLastName.text.toString()
            params["gender"] = binding.etGender.text.toString()
            params["birth_date"] = binding.etBirthday.text.toString()
            params["student_id"] = binding.etStudentID.text.toString()
            params["email"] = binding.etEmail.text.toString()
            profileApiUtils.saveProfile(params)
        }
    }


    private fun updateViews(user: User) {

        if (user.first_name != "") {
            binding.etFirstName.setText(user.first_name)
        }
        if (user.last_name != "") {
            binding.etLastName.setText(user.last_name)
        }
        if (user.student_id != "") {
            binding.etStudentID.setText(user.student_id)
        }
        if (user.gender != "") {
            binding.etGender.setText(user.gender)
        }
        if (user.birth_date != "") {
            binding.etBirthday.setText(user.birth_date)
        }
        if (user.username != "") {
            binding.etEmail.setText(user.email)
        }
    }

    override fun <T> result(apiResult: String, response: T?) {

        when (apiResult) {
            ProfileApiUtils.API_SUCCESS -> {
                user = response as User

                updateViews(user)
            }
            ProfileApiUtils.POST_SUCCESS -> {
                Toast.makeText(requireContext(), "Save Successfully.", Toast.LENGTH_SHORT).show()
                isEdit = false
                hideShow()
            }

        }

    }

}