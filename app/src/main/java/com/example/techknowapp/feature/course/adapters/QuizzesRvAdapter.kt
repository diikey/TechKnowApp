package com.example.techknowapp.feature.course.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.techknowapp.R
import com.example.techknowapp.core.model.Quiz
import com.example.techknowapp.core.model.QuizTaken
import com.example.techknowapp.core.utils.Cache
import com.example.techknowapp.databinding.ListQuizzesBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class QuizzesRvAdapter(
    private val onListUpdate: () -> Unit,
    private val onTakeQuiz: (Quiz) -> Unit,
    private val cache: Cache
) : RecyclerView.Adapter<QuizzesRvAdapter.ViewHolder>() {

    class ViewHolder(
        private val binding: ListQuizzesBinding,
        private val onTakeQuiz: (Quiz) -> Unit,
        private val cache: Cache
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Quiz) {
            binding.tvQuizName.text = data.name
            if (data.is_active) {
                binding.ivQuizIsLock.setImageResource(R.drawable.baseline_lock_open_24)
            } else {
                binding.ivQuizIsLock.setImageResource(R.drawable.baseline_lock_24)
            }

            val type = object : TypeToken<ArrayList<QuizTaken>>() {}.type
            val listOfQuizTaken =
                Gson().fromJson<ArrayList<QuizTaken>>(cache.getString(Cache.QUIZ_TAKEN, "[]"), type)
            val quizIndex = listOfQuizTaken.indexOfFirst { it.quiz_id == data.id.toString() }
            if (quizIndex != -1) {
                val score = listOfQuizTaken[quizIndex].quiz_score
                val quizSize = listOfQuizTaken[quizIndex].quiz_length
                binding.tvQuizScore.visibility = View.VISIBLE
                binding.tvQuizScore.text = "Score: $score/$quizSize"
            } else {
                binding.tvQuizScore.visibility = View.GONE
            }

            binding.linQuiz.setOnClickListener {
                onTakeQuiz(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ListQuizzesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding, onTakeQuiz, cache)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = differ.currentList[holder.adapterPosition]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private val diffCallBack = object : DiffUtil.ItemCallback<Quiz>() {
        override fun areContentsTheSame(oldItem: Quiz, newItem: Quiz): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: Quiz, newItem: Quiz): Boolean {
            return oldItem.id == newItem.id
        }
    }
    private val differ = AsyncListDiffer(this, diffCallBack)

    fun updateItems(newItems: List<Quiz>) {
        differ.submitList(newItems) {
            onListUpdate()
        }
    }

    fun getItems(): List<Quiz> {
        return differ.currentList
    }
}