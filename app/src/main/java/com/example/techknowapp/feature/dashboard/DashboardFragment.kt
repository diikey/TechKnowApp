package com.example.techknowapp.feature.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.techknowapp.databinding.FragmentDashboardBinding
import com.example.techknowapp.feature.dashboard.adapters.ClassRvAdapter

class DashboardFragment : Fragment() {

    private lateinit var binding: FragmentDashboardBinding
    private lateinit var adapter: ClassRvAdapter

    companion object {
        const val TAG = "DashboardFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDashboardBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initComponents()
    }

    private fun initComponents() {
        /**
         * RECYCLER VIEW ADAPTER
         */
        val list = listOf("Class 1", "Class 2iahsdihavdiva ahsvdia db", "Class 3", "Class 4", "Class 5", "Class 6")
        binding.rvClassList.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        adapter = ClassRvAdapter()
        adapter.onAttachedToRecyclerView(binding.rvClassList)
        binding.rvClassList.adapter = adapter
        binding.rvClassList.setHasFixedSize(true)
        adapter.updateItems(list)
    }
}