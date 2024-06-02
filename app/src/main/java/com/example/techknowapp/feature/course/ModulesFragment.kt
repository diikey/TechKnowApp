package com.example.techknowapp.feature.course

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.techknowapp.core.model.Course
import com.example.techknowapp.core.model.CourseModule
import com.example.techknowapp.databinding.FragmentModulesBinding
import com.example.techknowapp.feature.course.adapters.ModulesRvAdapter
import com.example.techknowapp.feature.course.dialogs.DownloadModuleDialog
import com.example.techknowapp.feature.course.utils.CourseApiCallback
import com.example.techknowapp.feature.course.utils.CourseApiUtils

class ModulesFragment(private val course: Course) : Fragment(), CourseApiCallback {

    private lateinit var binding: FragmentModulesBinding
    private lateinit var courseApiUtils: CourseApiUtils
    private lateinit var adapter: ModulesRvAdapter

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

        initComponents()
        initApiCall()
    }

    private fun initComponents() {
        /**
         * RECYCLER VIEW
         */
        binding.rvModules.layoutManager = LinearLayoutManager(requireContext())
        adapter = ModulesRvAdapter(
            onListUpdate = {
                modulesListCallback()
            },
            downloadModule = { data ->
                if (!data.is_active) {
                    Toast.makeText(requireContext(), "Module is Locked!", Toast.LENGTH_SHORT).show()
                    return@ModulesRvAdapter
                }
                openModuleDialog(data)
            }
        )
        adapter.onAttachedToRecyclerView(binding.rvModules)
        binding.rvModules.adapter = adapter
    }

    private fun initApiCall() {
        hideShowLoading(true)
        val params = HashMap<String, String>()
        params["course_code"] = course.course_code

        courseApiUtils.getCourseModules(params)
    }

    private fun hideShowLoading(isShow: Boolean) {
        if (isShow) {
            binding.rvModules.visibility = View.GONE
            binding.tvNoModules.visibility = View.GONE
            binding.pbLoadingModules.visibility = View.VISIBLE
            return
        }
        modulesListCallback()
        binding.pbLoadingModules.visibility = View.GONE
    }

    private fun modulesListCallback() {
        if (adapter.itemCount == 0) {
            binding.rvModules.visibility = View.GONE
            binding.tvNoModules.visibility = View.VISIBLE
            return
        }
        binding.rvModules.visibility = View.VISIBLE
        binding.tvNoModules.visibility = View.GONE
    }

    private fun openModuleDialog(courseModule: CourseModule) {
        val downloadModuleDialog = DownloadModuleDialog(
            onDownload = { file ->
                Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(file)
                    startActivity(this)
                }
            },
            courseModule = courseModule
        )

        downloadModuleDialog.isCancelable = true
        downloadModuleDialog.show(childFragmentManager, DownloadModuleDialog::class.java.simpleName)
    }

    override fun <T> result(apiResult: String, response: T?) {
        hideShowLoading(false)
        when (apiResult) {
            CourseApiUtils.API_SUCCESS -> {
                val modules = response as List<CourseModule>
                adapter.updateItems(modules)
            }
        }
    }
}