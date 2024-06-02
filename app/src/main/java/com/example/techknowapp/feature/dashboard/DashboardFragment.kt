package com.example.techknowapp.feature.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.techknowapp.R
import com.example.techknowapp.core.model.Course
import com.example.techknowapp.core.model.Announcement
import com.example.techknowapp.databinding.FragmentDashboardBinding
import com.example.techknowapp.feature.dashboard.adapters.AnnouncementsRvAdapter
import com.example.techknowapp.feature.dashboard.adapters.ClassRvAdapter
import com.example.techknowapp.feature.dashboard.dialogs.JoinClassDialog
import com.example.techknowapp.feature.dashboard.utils.DashboardApiCallback
import com.example.techknowapp.feature.dashboard.utils.DashboardApiUtils
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class DashboardFragment : Fragment(), DashboardApiCallback {

    private lateinit var binding: FragmentDashboardBinding
    private lateinit var adapter: ClassRvAdapter
    private lateinit var announcementAdapter: AnnouncementsRvAdapter
    private lateinit var dashboardApiUtils: DashboardApiUtils
    private lateinit var joinClassDialog: JoinClassDialog

    companion object {
        const val TAG = "DashboardFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dashboardApiUtils = DashboardApiUtils(requireContext(), this)

        initComponents()
        CoroutineScope(Dispatchers.Main).launch {
            delay(100)
            initApiCall()
        }
    }

    private fun initComponents() {
        joinClassDialog = JoinClassDialog(this)

        /**
         * SWIPE REFRESH LAYOUT
         */
        binding.swipeRefreshLayout.setOnRefreshListener {
            initApiCall()
        }

        /**
         * RECYCLER VIEW ADAPTER
         */
        binding.rvClassList.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        adapter = ClassRvAdapter(
            onListUpdate = {
                classListCallback()
            },
            goToCourseDetails = { course ->
                goToCourseDetails(course)
            }
        )
        adapter.onAttachedToRecyclerView(binding.rvClassList)
        binding.rvClassList.adapter = adapter
        binding.rvClassList.setHasFixedSize(true)

        binding.rvAnnouncements.layoutManager = LinearLayoutManager(requireContext())
        announcementAdapter = AnnouncementsRvAdapter(
            onListUpdate = {
                announcementListCallback()
            },
            goToAnnouncementDetails = { announcement ->
                goToAnnouncementDetails(announcement)
            }
        )
        announcementAdapter.onAttachedToRecyclerView(binding.rvAnnouncements)
        binding.rvAnnouncements.adapter = announcementAdapter

        /**
         * ON CLICKS
         */
        binding.linJoinClass.setOnClickListener {
//            Intent(Intent.ACTION_VIEW).apply {
//                setData(Uri.parse("https://www.google.com/"))
//                startActivity(this)
//            }
            joinClassDialog.isCancelable = true
            joinClassDialog.show(childFragmentManager, JoinClassDialog::class.java.simpleName)
        }
    }

    private fun initApiCall() {
        hideShowLoading(true)
        dashboardApiUtils.getAnnouncements()
        dashboardApiUtils.getCourse()
    }

    private fun hideShowLoading(isShow: Boolean) {
        if (isShow) {
            binding.rvAnnouncements.visibility = View.GONE
            binding.tvNoAnnouncements.visibility = View.GONE
            binding.pbLoadingAnnouncements.visibility = View.VISIBLE

            binding.rvClassList.visibility = View.GONE
            binding.linJoinClass.visibility = View.GONE
            binding.pbLoadingClasses.visibility = View.VISIBLE
            return
        }
        announcementListCallback()
        binding.pbLoadingAnnouncements.visibility = View.GONE
        classListCallback()
        binding.pbLoadingClasses.visibility = View.GONE
    }

    private fun classListCallback() {
        if (adapter.itemCount == 0) {
            binding.rvClassList.visibility = View.GONE
            binding.linJoinClass.visibility = View.VISIBLE
            return
        }
        binding.rvClassList.visibility = View.VISIBLE
        binding.linJoinClass.visibility = View.GONE
    }

    private fun announcementListCallback() {
        if (announcementAdapter.itemCount == 0) {
            binding.rvAnnouncements.visibility = View.GONE
            binding.tvNoAnnouncements.visibility = View.VISIBLE
            return
        }
        binding.rvAnnouncements.visibility = View.VISIBLE
        binding.tvNoAnnouncements.visibility = View.GONE
    }

    private fun failedApiToast() {
        Toast.makeText(
            requireContext(),
            "Something went wrong, please try again,",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun goToCourseDetails(course: Course) {
        val bundle = bundleOf("course" to Gson().toJson(course))
        findNavController().navigate(R.id.action_DashboardFragment_to_CourseFragment, bundle)
    }

    private fun goToAnnouncementDetails(announcement: Announcement) {
        val bundle = bundleOf("announcement" to Gson().toJson(announcement))
        findNavController().navigate(R.id.action_DashboardFragment_to_CourseFragment, bundle)
    }

    override fun <T> result(apiResult: String, response: T?) {
        binding.swipeRefreshLayout.isRefreshing = false
        hideShowLoading(false)
        Timber.d("dashboard result>>>$apiResult")
        when (apiResult) {
            DashboardApiUtils.API_SUCCESS -> {
                val listCourse = response as List<Course>
                adapter.updateItems(listCourse)
            }

            DashboardApiUtils.API_FAILED -> {
//                failedApiToast()
            }

            DashboardApiUtils.APPLICATION_SUCCESS -> {
                Toast.makeText(requireContext(), "Class for approval!", Toast.LENGTH_SHORT).show()
                joinClassDialog.hideShowLoading(false)
                joinClassDialog.dismissAllowingStateLoss()
                initApiCall()
            }

            DashboardApiUtils.APPLICATION_FAILED -> {
                joinClassDialog.hideShowLoading(false)
                failedApiToast()
            }

            DashboardApiUtils.ANNOUNCEMENT_SUCCESS -> {
                val listAnnouncements = response as List<Announcement>
                announcementAdapter.updateItems(listAnnouncements)
            }

            DashboardApiUtils.ANNOUNCEMENT_FAILED -> {
//                failedApiToast()
            }
        }
    }
}