package com.example.techknowapp.feature.course.dialogs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.techknowapp.R
import com.example.techknowapp.core.model.Announcement
import com.example.techknowapp.databinding.FragmentNewsDialogBinding


class NewsDialog(private val announcement: Announcement) : DialogFragment() {
    private lateinit var binding: FragmentNewsDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentNewsDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        initComponents()
    }

    private fun initComponents() {
        binding.tvTitle.text = announcement.title
        binding.tvDescription.text = announcement.description
    }
}