package com.example.techknowapp.feature.course.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.techknowapp.core.model.CourseModule
import com.example.techknowapp.databinding.DialogDownloadModuleBinding

class DownloadModuleDialog(
    private val onDownload: (String) -> Unit,
    private val courseModule: CourseModule
) : DialogFragment() {

    private lateinit var binding: DialogDownloadModuleBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogDownloadModuleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initComponents()
    }

    private fun initComponents() {
        binding.tvModuleTitle.text = courseModule.name
        binding.tvModuleDescription.text = courseModule.description
        binding.btnDownloadModule.setOnClickListener {
            onDownload(courseModule.module_file)
        }
    }
}