package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.repository.QuizRepository
import kotlinx.coroutines.launch

class QuizViewModel : ViewModel() {

    private val repo = QuizRepository()
    private var userId: String? = null

    private val _score = MutableLiveData(0)
    val score: LiveData<Int> = _score

    private val _bestScore = MutableLiveData(0)
    val bestScore: LiveData<Int> = _bestScore

    private val _quizzesTaken = MutableLiveData(0)
    val quizzesTaken: LiveData<Int> = _quizzesTaken

    private val _scoreHistory = MutableLiveData<List<Pair<String, Int>>>(emptyList())
    val scoreHistory: LiveData<List<Pair<String, Int>>> = _scoreHistory

    fun setUserId(id: String) {
        userId = id
    }

    fun setScore(value: Int) {
        _score.value = value
    }

    fun saveResult() {
        val uid = userId ?: return
        viewModelScope.launch {
            val currentScore = _score.value ?: 0

            val best = repo.getBestScore(uid)
            if (currentScore > best) {
                repo.saveBestScore(uid, currentScore)
                _bestScore.value = currentScore
            } else {
                _bestScore.value = best
            }

            repo.incrementQuizzesTaken(uid)
            _quizzesTaken.value = repo.getQuizzesTaken(uid)

            repo.saveQuizAttempt(uid, currentScore)
            _scoreHistory.value = repo.getQuizHistory(uid)
        }
    }

    fun fetchUserStats() {
        val uid = userId ?: return
        viewModelScope.launch {
            _bestScore.value = repo.getBestScore(uid)
            _quizzesTaken.value = repo.getQuizzesTaken(uid)
            _scoreHistory.value = repo.getQuizHistory(uid)
        }
    }

    fun fetchAllScores() {
        val uid = userId ?: return
        viewModelScope.launch {
            _scoreHistory.value = repo.getQuizHistory(uid)
        }
    }
}
