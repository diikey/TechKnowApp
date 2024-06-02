package com.example.techknowapp.feature.course

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.load
import com.example.techknowapp.core.model.Course
import com.example.techknowapp.databinding.FragmentCourseBinding
import com.example.techknowapp.feature.course.adapters.CourseVpAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import timber.log.Timber

class CourseFragment : Fragment() {

    private lateinit var binding: FragmentCourseBinding
    private lateinit var viewPagerAdapter: CourseVpAdapter
    private lateinit var course: Course

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCourseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val bundle = arguments
        val courseString = bundle!!.getString("course")!!
        Timber.d("course>>>$courseString")
        val typeCourse = object : TypeToken<Course>() {}.type
        course = Gson().fromJson(courseString, typeCourse)

        initComponents()
    }

    private fun initComponents() {
        /**
         * IMAGEVIEW
         */
        binding.ivCourseImage.load(course.image)

        /**
         * TAB LAYOUT AND VIEW PAGER
         */
        viewPagerAdapter = CourseVpAdapter(this, course)
        binding.viewPager.adapter = viewPagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Modules"
                1 -> "Quizzes"
                2 -> "News"
                else -> ""
            }
        }.attach()
    }
}