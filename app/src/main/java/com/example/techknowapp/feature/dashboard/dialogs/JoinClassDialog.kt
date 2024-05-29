package com.example.techknowapp.feature.dashboard.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.techknowapp.databinding.DialogJoinClassBinding
import com.example.techknowapp.feature.dashboard.utils.DashboardApiUtils

class JoinClassDialog(private val fragment: Fragment) : DialogFragment() {
    private lateinit var binding: DialogJoinClassBinding
    private lateinit var dashboardApiUtils: DashboardApiUtils

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogJoinClassBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dashboardApiUtils = DashboardApiUtils(requireContext(), fragment)
        initComponents()
    }

    private fun initComponents() {
        binding.btnJoinClass.setOnClickListener {
            hideShowLoading(true)
            val params = HashMap<String, String>()
            params["course_code"] = binding.etCourseCode.text.toString()

            dashboardApiUtils.applyCourse(params)
        }
    }

    fun hideShowLoading(isShow: Boolean) {
        if (isShow) {
            dialog!!.setCancelable(false)
            binding.btnJoinClass.visibility = View.GONE
            binding.pbLoadingJoin.visibility = View.VISIBLE
            return
        }
        dialog!!.setCancelable(true)
        binding.btnJoinClass.visibility = View.VISIBLE
        binding.pbLoadingJoin.visibility = View.GONE
    }
}