package com.example.myapplication.quiz

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.adapter.ScoreHistoryAdapter
import com.example.myapplication.databinding.PreviousResultActivityBinding
import com.example.myapplication.launch.LoginActivity
import com.example.myapplication.SharedPrefManager
import com.example.myapplication.viewmodel.QuizViewModel
import android.content.Intent

class QuizHistoryActivity : AppCompatActivity() {

    private lateinit var binding: PreviousResultActivityBinding
    private val quizViewModel: QuizViewModel by viewModels()
    private lateinit var sharedPrefManager: SharedPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PreviousResultActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            window.statusBarColor = android.graphics.Color.WHITE
            window.navigationBarColor = android.graphics.Color.WHITE
            window.decorView.systemUiVisibility =
                android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or
                        android.view.View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        } else {
            window.statusBarColor = android.graphics.Color.BLACK
            window.navigationBarColor = android.graphics.Color.BLACK
        }


        sharedPrefManager = SharedPrefManager(this)
        val userId = sharedPrefManager.getUserId()
        if (userId == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }
        quizViewModel.setUserId(userId)

        binding.recyclerViewHistory.layoutManager = LinearLayoutManager(this)
        val adapter = ScoreHistoryAdapter(emptyList())
        binding.recyclerViewHistory.adapter = adapter

        quizViewModel.scoreHistory.observe(this) { history ->
            adapter.updateScores(history)
        }

        quizViewModel.fetchAllScores()

        binding.ivBack.setOnClickListener {
            finish()
        }
    }
}
