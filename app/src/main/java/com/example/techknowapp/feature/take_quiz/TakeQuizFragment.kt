package com.example.techknowapp.feature.take_quiz

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.techknowapp.core.model.Quiz
import com.example.techknowapp.databinding.FragmentTakeQuizBinding
import com.example.techknowapp.feature.take_quiz.utils.TakeQuizApiCallback
import com.example.techknowapp.feature.take_quiz.utils.TakeQuizApiUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TakeQuizFragment : Fragment(), TakeQuizApiCallback {

    private lateinit var binding: FragmentTakeQuizBinding
    private lateinit var quiz: Quiz
    private lateinit var takeQuizApiUtils: TakeQuizApiUtils

    val timer = object : CountDownTimer(20000, 1000) {
        override fun onTick(millisUntilFinished: Long) {}

        override fun onFinish() {}
    }
//    timer.start()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTakeQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val bundle = arguments
        val quizString = bundle!!.getString("quiz")!!
        val typeQuiz = object : TypeToken<Quiz>() {}.type
        quiz = Gson().fromJson(quizString, typeQuiz)

        takeQuizApiUtils = TakeQuizApiUtils(requireContext(), this)

        initComponents()
        initApiCall()
    }

    private fun initComponents() {
        //
    }

    private fun initApiCall() {
        val params = HashMap<String, String>()
        params["quiz_id"] = quiz.id.toString()

        takeQuizApiUtils.getQuestions(params)
    }

    override fun <T> result(apiResult: String, response: T?) {
        binding.linQuizLoading.visibility = View.GONE
        binding.btnStartQuiz.visibility = View.VISIBLE
        when (apiResult) {
            TakeQuizApiUtils.API_SUCCESS -> {}
            TakeQuizApiUtils.API_FAILED -> {}
        }
    }
}