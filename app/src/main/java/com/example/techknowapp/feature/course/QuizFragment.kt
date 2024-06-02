package com.example.techknowapp.feature.course

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.techknowapp.R
import com.example.techknowapp.core.model.Course
import com.example.techknowapp.core.model.Quiz
import com.example.techknowapp.databinding.FragmentQuizBinding
import com.example.techknowapp.feature.course.adapters.QuizzesRvAdapter
import com.example.techknowapp.feature.course.utils.CourseApiCallback
import com.example.techknowapp.feature.course.utils.CourseApiUtils
import com.google.gson.Gson

class QuizFragment(private val course: Course) : Fragment(), CourseApiCallback {

    private lateinit var binding: FragmentQuizBinding
    private lateinit var courseApiUtils: CourseApiUtils
    private lateinit var adapter: QuizzesRvAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentQuizBinding.inflate(inflater, container, false)
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
        binding.rvQuizzes.layoutManager = LinearLayoutManager(requireContext())
        adapter = QuizzesRvAdapter(
            onListUpdate = {
                quizzesListCallback()
            },
            onTakeQuiz = { data ->
                if (!data.is_active) {
                    Toast.makeText(requireContext(), "Quiz is Locked!", Toast.LENGTH_SHORT).show()
                    return@QuizzesRvAdapter
                }
                takeQuizDialog(data)
            }
        )
        adapter.onAttachedToRecyclerView(binding.rvQuizzes)
        binding.rvQuizzes.adapter = adapter
    }

    private fun initApiCall() {
        hideShowLoading(true)
        val params = HashMap<String, String>()
        params["course_code"] = course.course_code

        courseApiUtils.getCourseQuizzes(params)
    }

    private fun hideShowLoading(isShow: Boolean) {
        if (isShow) {
            binding.rvQuizzes.visibility = View.GONE
            binding.tvNoQuizzes.visibility = View.GONE
            binding.pbLoadingQuizzes.visibility = View.VISIBLE
            return
        }
        quizzesListCallback()
        binding.pbLoadingQuizzes.visibility = View.GONE
    }

    private fun quizzesListCallback() {
        if (adapter.itemCount == 0) {
            binding.rvQuizzes.visibility = View.GONE
            binding.tvNoQuizzes.visibility = View.VISIBLE
            return
        }
        binding.rvQuizzes.visibility = View.VISIBLE
        binding.tvNoQuizzes.visibility = View.GONE
    }

    private fun takeQuizDialog(quiz: Quiz) {
        AlertDialog.Builder(requireContext()).apply {
            setTitle("Confirmation")
            setMessage("Do you want to take this quiz?")
            setPositiveButton("YES") { dialog, _ ->
                val bundle = bundleOf("quiz" to Gson().toJson(quiz))
                findNavController().navigate(R.id.action_CourseFragment_to_TakeQuizFragment, bundle)
            }
            setNegativeButton("NO") { dialog, _ ->
                dialog.dismiss()
            }
        }.show()
    }

    override fun <T> result(apiResult: String, response: T?) {
        hideShowLoading(false)
        when (apiResult) {
            CourseApiUtils.QUIZ_SUCCESS -> {
                val modules = response as List<Quiz>
                adapter.updateItems(modules)
            }
        }
    }
}