package com.example.techknowapp.feature.take_quiz

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.view.get
import com.example.techknowapp.core.model.Question
import com.example.techknowapp.core.model.Quiz
import com.example.techknowapp.core.model.QuizAnswer
import com.example.techknowapp.core.model.TakeQuizRes
import com.example.techknowapp.databinding.FragmentTakeQuizBinding
import com.example.techknowapp.feature.take_quiz.utils.TakeQuizApiCallback
import com.example.techknowapp.feature.take_quiz.utils.TakeQuizApiUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import timber.log.Timber
import java.util.Locale

class TakeQuizFragment : Fragment(), TakeQuizApiCallback {

    private lateinit var binding: FragmentTakeQuizBinding
    private lateinit var quiz: Quiz
    private lateinit var questions: List<Question>
    private lateinit var takeQuizApiUtils: TakeQuizApiUtils
    private lateinit var timer: CountDownTimer

    private var index = 0

    private val answerHash = HashMap<String, QuizAnswer>()

    companion object {
        const val COUNTDOWN_INTERVAL = 1000L
    }

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

        initComponents(view)
        initApiCall()
    }

    private fun initComponents(view: View) {
        binding.tvQuizTitle.text = quiz.name
        binding.btnStartQuiz.setOnClickListener {
            binding.linStartQuiz.visibility = View.GONE
            binding.linQuizQuestions.visibility = View.VISIBLE

            setQuestion()
            timer.start()
        }

        binding.rgAnswers.setOnCheckedChangeListener { group, checkedId ->
            Timber.d("checked id>>>$checkedId")
            if (checkedId == -1) {
                answerHash["answer${index}"] = QuizAnswer(answer = "", index = index)
                return@setOnCheckedChangeListener
            }
            val selectedRb = view.findViewById<RadioButton>(checkedId)
//            answerList[index] = selectedRb.text.toString()
//            Timber.d("answers>>>${Gson().toJson(answerList)}")
            answerHash["answer${index}"] = QuizAnswer(
                answer = selectedRb.text.toString(),
                index = index
            )
            Timber.d("answers>>>${Gson().toJson(answerHash)}")
        }

        binding.btnPrevQuestion.setOnClickListener {
            Timber.d("prev>>>$index>>>${questions.size}")
            if (index == 0) return@setOnClickListener
            index--
            setQuestion()
        }

        binding.btnNextQuestion.setOnClickListener {
            Timber.d("next>>>$index>>>${questions.size}")
            index++
            if (index >= questions.size) {
                index--
                return@setOnClickListener
            }
            setQuestion()
        }

        binding.btnFinishQuiz.setOnClickListener {
            computeScore()
        }
    }

    private fun setQuestion() {
        binding.tvNumOfQuestions.text = "Question: ${index + 1}/${questions.size}"
        binding.tvQuestionDescription.text = questions[index].question
        binding.rb1.text = questions[index].option1
        binding.rb2.text = questions[index].option2
        binding.rb3.text = questions[index].option3
        binding.rb4.text = questions[index].option4
        Timber.d("current ans>>>${answerHash["answer${index}"]}")
        if (answerHash["answer${index}"]!!.answer != "") {
            for (i in 0 until 4) {
                if (answerHash["answer${index}"]!!.answer == getRbPerIndex(i)) {
                    binding.rgAnswers.check(binding.rgAnswers[i].id)
                }
            }
        } else {
            binding.rgAnswers.clearCheck()
        }
    }

    private fun computeScore() {
        var score = 0
        for (i in questions.indices) {
            if (questions[i].answer == answerHash["answer${i}"]!!.answer) {
                score++
            }
        }

        Timber.d("final score>>>>>>>>$score")
        timer.cancel()
    }

    private fun getRbPerIndex(index: Int): String {
        return when (index) {
            0 -> {
                binding.rb1.text.toString()
            }

            1 -> {
                binding.rb2.text.toString()
            }

            2 -> {
                binding.rb3.text.toString()
            }

            3 -> {
                binding.rb4.text.toString()
            }

            else -> {
                ""
            }
        }
    }

    private fun computeTimer() {
        val totalTimeInMillis = questions.size * 60 * COUNTDOWN_INTERVAL
        timer = object : CountDownTimer(totalTimeInMillis, COUNTDOWN_INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = (millisUntilFinished / 1000) % 60
                val minutesRemaining = (millisUntilFinished / 1000) / 60
                val totalMinutes = questions.size
                val totalSeconds = 0
                binding.tvTimer.text = String.format(
                    Locale.getDefault(),
                    "%02d:%02d / %02d:%02d",
                    minutesRemaining,
                    secondsRemaining,
                    totalMinutes,
                    totalSeconds
                )
            }

            override fun onFinish() {
                Timber.d("timer finished>>>>>")
                computeScore()
            }
        }
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
            TakeQuizApiUtils.API_SUCCESS -> {
                val takeQuiz = response as TakeQuizRes
                questions = takeQuiz.questions

                for (i in questions.indices) {
                    answerHash["answer${i}"] = QuizAnswer(
                        answer = "",
                        index = i
                    )
                }

                Timber.d("list size>>>${answerHash.size}")

                val secondsRemaining = ((60000 * questions.size) / 1000) % 60
                val minutesRemaining = ((60000 * questions.size) / 1000) / 60
                val totalMinutes = questions.size
                val totalSeconds = 0
                binding.tvTimer.text = String.format(
                    Locale.getDefault(),
                    "%02d:%02d / %02d:%02d",
                    minutesRemaining,
                    secondsRemaining,
                    totalMinutes,
                    totalSeconds
                )
                computeTimer()
            }

            TakeQuizApiUtils.API_FAILED -> {}
        }
    }
}