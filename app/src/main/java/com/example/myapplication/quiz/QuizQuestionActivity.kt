package com.example.myapplication.quiz

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.model.Question
import com.example.myapplication.viewmodel.QuizViewModel
import com.google.android.material.button.MaterialButton

class QuizQuestionActivity : AppCompatActivity() {

    private val quizViewModel: QuizViewModel by viewModels()


    private lateinit var questionNumberText: TextView
    private lateinit var timerText: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var questionText: TextView
    private lateinit var radioGroup: RadioGroup
    private lateinit var option1: RadioButton
    private lateinit var option2: RadioButton
    private lateinit var option3: RadioButton
    private lateinit var option4: RadioButton
    private lateinit var nextBtn: MaterialButton
    private lateinit var backButton: MaterialButton

    private val questions: List<Question> by lazy {
        listOf(
            Question("What is 2 + 2?", listOf("3", "4", "5", "6"), 1),
            Question("Capital of France?", listOf("Berlin", "Madrid", "Paris", "Rome"), 2),
            Question("Kotlin is developed by?", listOf("Google", "JetBrains", "Microsoft", "Meta"), 1),
            Question("Android UI toolkit?", listOf("SwiftUI", "Compose", "Flutter", "UIKit"), 1),
            Question("Android layout file type?", listOf(".kt", ".java", ".xml", ".gradle"), 2)
        )
    }

    private var currentIndex = 0
    private val totalSeconds = 10
    private var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_question)
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


        questionNumberText = findViewById(R.id.questionNumberText)
        timerText = findViewById(R.id.timerText)
        progressBar = findViewById(R.id.progressBar)
        questionText = findViewById(R.id.questionText)
        radioGroup = findViewById(R.id.radioGroup)
        option1 = findViewById(R.id.option1)
        option2 = findViewById(R.id.option2)
        option3 = findViewById(R.id.option3)
        option4 = findViewById(R.id.option4)
        nextBtn = findViewById(R.id.nextBtn)
        backButton = findViewById(R.id.backButton)

        progressBar.max = totalSeconds
        quizViewModel.setScore(0)

        loadQuestion()

        nextBtn.setOnClickListener { submitAnswerAndGoNext() }
        backButton.setOnClickListener { finish() }
    }

    private fun loadQuestion() {
        cancelTimer()

        val question = questions[currentIndex]
        questionNumberText.text = "Question ${currentIndex + 1} of ${questions.size}"
        questionText.text = question.question
        option1.text = question.options[0]
        option2.text = question.options[1]
        option3.text = question.options[2]
        option4.text = question.options[3]
        radioGroup.clearCheck()

        progressBar.progress = totalSeconds
        timerText.text = "Time: ${totalSeconds}s"
        startTimer()
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(totalSeconds * 1000L, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                val secs = (millisUntilFinished / 1000L).toInt()
                timerText.text = "Time: ${secs}s"
                progressBar.progress = secs
            }

            override fun onFinish() {
                progressBar.progress = 0
                submitAnswerAndGoNext(timeUp = true)
            }
        }.start()
    }

    private fun submitAnswerAndGoNext(timeUp: Boolean = false) {
        cancelTimer()

        val selectedIndex = when (radioGroup.checkedRadioButtonId) {
            R.id.option1 -> 0
            R.id.option2 -> 1
            R.id.option3 -> 2
            R.id.option4 -> 3
            else -> -1
        }

        val correctIndex = questions[currentIndex].correctAnswerIndex

        if (!timeUp && selectedIndex == correctIndex) {
            val currentScore = quizViewModel.score.value ?: 0
            quizViewModel.setScore(currentScore + 1)
        }

        if (currentIndex < questions.lastIndex) {
            currentIndex++
            loadQuestion()
        } else {
            endQuiz()
        }
    }

    private fun endQuiz() {
        val finalScore = quizViewModel.score.value ?: 0
        val intent = Intent(this, QuizResultActivity::class.java)
        intent.putExtra("finalScore", finalScore)
        startActivity(intent)
        finish()
    }

    private fun cancelTimer() {
        countDownTimer?.cancel()
        countDownTimer = null
    }

    override fun onDestroy() {
        super.onDestroy()
        cancelTimer()
    }
}
