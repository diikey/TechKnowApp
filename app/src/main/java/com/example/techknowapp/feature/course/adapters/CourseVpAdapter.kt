package com.example.techknowapp.feature.course.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.techknowapp.feature.course.ModulesFragment

class CourseVpAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ModulesFragment()
            1 -> ModulesFragment()
            2 -> ModulesFragment()
            else -> throw IllegalStateException("Invalid posistion $position")
        }
    }
}