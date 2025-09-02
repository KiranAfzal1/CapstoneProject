package com.example.myapplication.quiz

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.launch.LoginActivity
import com.example.myapplication.SharedPrefManager
import com.example.myapplication.viewmodel.QuizViewModel
import kotlinx.coroutines.launch

class QuizResultActivity : AppCompatActivity() {

    private val quizViewModel: QuizViewModel by viewModels()
    private lateinit var sharedPrefManager: SharedPrefManager

    private lateinit var scoreText: TextView
    private lateinit var percentageText: TextView
    private lateinit var bestScoreText: TextView
    private lateinit var messageText: TextView
    private lateinit var backButton: Button
    private lateinit var btnViewHistory: Button

    companion object {
        private const val TOTAL_QUESTIONS = 5
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_result)
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

        scoreText = findViewById(R.id.scoreText)
        percentageText = findViewById(R.id.percentageText)
        bestScoreText = findViewById(R.id.bestScoreText)
        messageText = findViewById(R.id.messageText)
        backButton = findViewById(R.id.backButton)
        btnViewHistory = findViewById(R.id.btnViewHistory)

        val finalScore = intent.getIntExtra("finalScore", 0)
        quizViewModel.setScore(finalScore)
        displayScore(finalScore)

        lifecycleScope.launch {
            try {
                quizViewModel.saveResult()
                quizViewModel.fetchUserStats()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        observeViewModel()

        backButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        btnViewHistory.setOnClickListener {
            startActivity(Intent(this, QuizHistoryActivity::class.java))
        }
    }

    private fun displayScore(score: Int) {
        val percentage = (score.toFloat() / TOTAL_QUESTIONS * 100).toInt()
        scoreText.text = "You scored $score out of $TOTAL_QUESTIONS"
        percentageText.text = "$percentage%"
        messageText.text = when {
            percentage == 100 -> "Outstanding! Perfect score!"
            percentage >= 80 -> "Excellent work!"
            percentage >= 50 -> "Good job!"
            else -> "Don't worry, you'll improve next time!"
        }
    }

    private fun observeViewModel() {
        quizViewModel.bestScore.observe(this) { best ->
            bestScoreText.text = "Best Score: $best"
        }
     
    }
}
