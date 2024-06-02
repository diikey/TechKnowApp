package com.example.techknowapp.feature.course.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.techknowapp.core.model.Course
import com.example.techknowapp.feature.course.ModulesFragment
import com.example.techknowapp.feature.course.NewsFragment
import com.example.techknowapp.feature.course.QuizFragment

class CourseVpAdapter(fragment: Fragment, private val course: Course) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ModulesFragment(course)
            1 -> QuizFragment()
            2 -> NewsFragment()
            else -> throw IllegalStateException("Invalid posistion $position")
        }
    }
}