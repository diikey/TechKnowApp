package com.example.techknowapp.feature.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.techknowapp.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private lateinit var binding: FragmentDashboardBinding

    companion object {
        const val TAG = "DashboardFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDashboardBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //todo
    }
}