package com.example.techknowapp.feature.course

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.techknowapp.core.model.Announcement
import com.example.techknowapp.core.model.Course
import com.example.techknowapp.databinding.FragmentNewsBinding
import com.example.techknowapp.feature.course.adapters.NewsRvAdapter
import com.example.techknowapp.feature.course.dialogs.NewsDialog
import com.example.techknowapp.feature.course.utils.CourseApiCallback
import com.example.techknowapp.feature.course.utils.CourseApiUtils

class NewsFragment(private val course: Course) : Fragment(), CourseApiCallback {

    private lateinit var binding: FragmentNewsBinding
    private lateinit var courseApiUtils: CourseApiUtils
    private lateinit var adapter: NewsRvAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        courseApiUtils = CourseApiUtils(requireContext(), this)

        initComponents()
        initApiCalls()
    }

    private fun initComponents() {
        binding.rvNews.layoutManager = LinearLayoutManager(requireContext())
        adapter = NewsRvAdapter(onListUpdate = {
            modulesListCallback()
        },
        onItemClick = {
            val dialog = NewsDialog(it)

            dialog.isCancelable = true
            dialog.show(childFragmentManager, NewsDialog::class.java.simpleName)
        })
        adapter.onAttachedToRecyclerView(binding.rvNews)
        binding.rvNews.adapter = adapter
    }

    private fun initApiCalls() {
        hideShowLoading(true)
        val params = HashMap<String, String>()
        params["course_code"] = course.course_code

        courseApiUtils.getCourseAnnouncements(params)
    }

    private fun hideShowLoading(isShow: Boolean) {
        if (isShow) {
            binding.rvNews.visibility = View.GONE
            binding.tvNoNews.visibility = View.GONE
            binding.pbLoadingNews.visibility = View.VISIBLE
            return
        }
        modulesListCallback()
        binding.pbLoadingNews.visibility = View.GONE
    }

    private fun modulesListCallback() {
        if (adapter.itemCount == 0) {
            binding.rvNews.visibility = View.GONE
            binding.tvNoNews.visibility = View.VISIBLE
            return
        }
        binding.rvNews.visibility = View.VISIBLE
        binding.tvNoNews.visibility = View.GONE
    }

    override fun <T> result(apiResult: String, response: T?) {
        hideShowLoading(false)
        when(apiResult){
            CourseApiUtils.NEWS_SUCCESS->{
                val modules = response as List<Announcement>
                adapter.updateItems(modules)
            }
        }
    }
}