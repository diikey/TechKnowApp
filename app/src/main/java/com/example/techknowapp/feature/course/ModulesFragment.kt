package com.example.techknowapp.feature.course

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.techknowapp.core.model.Course
import com.example.techknowapp.databinding.FragmentModulesBinding
import com.example.techknowapp.feature.course.utils.CourseApiCallback
import com.example.techknowapp.feature.course.utils.CourseApiUtils

class ModulesFragment(private val course: Course) : Fragment(), CourseApiCallback {

    private lateinit var binding: FragmentModulesBinding
    private lateinit var courseApiUtils: CourseApiUtils

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentModulesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        courseApiUtils = CourseApiUtils(requireContext(), this)

        initApiCall()
    }

    private fun initApiCall() {
        val params = HashMap<String, String>()
        params["course_code"] = course.course_code

        courseApiUtils.getCourseModules(params)
    }

    override fun <T> result(apiResult: String, response: T?) {
        when (apiResult) {
            CourseApiUtils.API_SUCCESS -> {}
        }
    }
}