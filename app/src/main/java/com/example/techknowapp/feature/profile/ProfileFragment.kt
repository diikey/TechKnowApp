package com.example.techknowapp.feature.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.techknowapp.R
import com.example.techknowapp.databinding.FragmentProfileBinding
import timber.log.Timber


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    private var isEdit = false

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

        initViews()
        initOnClicks()
    }

    private fun initViews(){
        hideShow()
    }

    private fun hideShow(){
        if (isEdit){
            binding.etBirthday.isEnabled = true
            binding.etGender.isEnabled = true
            binding.etFirstName.isEnabled = true
            binding.etLastName.isEnabled = true
            binding.etStudentID.isEnabled = true
            binding.btnSave.visibility = View.VISIBLE
            binding.btnCancel.visibility = View.VISIBLE
            binding.btnEdit.visibility = View.GONE
        }else{
            binding.etBirthday.isEnabled = false
            binding.etGender.isEnabled = false
            binding.etFirstName.isEnabled = false
            binding.etLastName.isEnabled = false
            binding.etStudentID.isEnabled = false
            binding.btnSave.visibility = View.GONE
            binding.btnCancel.visibility = View.GONE
            binding.btnEdit.visibility = View.VISIBLE
        }
    }

    private fun initOnClicks(){
        binding.btnEdit.setOnClickListener {
            isEdit = !isEdit
            hideShow()
        }
        binding.btnCancel.setOnClickListener {
            isEdit = false
            hideShow()
        }
    }

}