package com.example.techknowapp.feature.dashboard

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.techknowapp.databinding.FragmentDashboardBinding
import com.example.techknowapp.feature.dashboard.adapters.ClassRvAdapter
import com.example.techknowapp.feature.dashboard.utils.DashboardApiCallback
import com.example.techknowapp.feature.dashboard.utils.DashboardApiUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class DashboardFragment : Fragment(), DashboardApiCallback {

    private lateinit var binding: FragmentDashboardBinding
    private lateinit var adapter: ClassRvAdapter
    private lateinit var dashboardApiUtils: DashboardApiUtils

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
        /**
         * SWIPE REFRESH LAYOUT
         */
        binding.swipeRefreshLayout.setOnRefreshListener {
            initApiCall()
        }

        /**
         * RECYCLER VIEW ADAPTER
         */
        val list = listOf(
            "Class 1",
            "Class 2iahsdihavdiva ahsvdia db",
            "Class 3",
            "Class 4",
            "Class 5",
            "Class 6"
        )
        binding.rvClassList.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        adapter = ClassRvAdapter()
        adapter.onAttachedToRecyclerView(binding.rvClassList)
        binding.rvClassList.adapter = adapter
        binding.rvClassList.setHasFixedSize(true)
        adapter.updateItems(list)

        /**
         * ON CLICKS
         */
        binding.linJoinClass.setOnClickListener {
            Intent(Intent.ACTION_VIEW).apply {
                setData(Uri.parse("https://www.google.com/"))
                startActivity(this)
            }
        }
    }

    private fun initApiCall() {
        hideShowLoading(true)
        dashboardApiUtils.getCourse()
    }

    private fun hideShowLoading(isShow: Boolean) {
        if (isShow) {
            binding.rvClassList.visibility = View.GONE
            binding.linJoinClass.visibility = View.GONE
            binding.pbLoadingClasses.visibility = View.VISIBLE
            return
        }
        binding.rvClassList.visibility = View.VISIBLE
        binding.linJoinClass.visibility = View.VISIBLE
        binding.pbLoadingClasses.visibility = View.GONE
    }

    override fun result(apiResult: String) {
        hideShowLoading(false)
        Timber.d("result>>>$apiResult")
    }
}