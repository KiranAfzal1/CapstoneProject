package com.example.myapplication.repository

import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class QuizRepository {

    private val db = FirebaseDatabase.getInstance(
        "https://socialapp-d9381-default-rtdb.asia-southeast1.firebasedatabase.app"
    ).getReference("users")

    suspend fun saveBestScore(userId: String, score: Int) {
        db.child(userId).child("bestScore").setValue(score).await()
    }

    suspend fun getBestScore(userId: String): Int {
        val snapshot = db.child(userId).child("bestScore").get().await()
        return snapshot.getValue(Int::class.java) ?: 0
    }

    suspend fun incrementQuizzesTaken(userId: String) {
        val snapshot = db.child(userId).child("quizzesTaken").get().await()
        val current = snapshot.getValue(Int::class.java) ?: 0
        db.child(userId).child("quizzesTaken").setValue(current + 1).await()
    }

    suspend fun getQuizzesTaken(userId: String): Int {
        val snapshot = db.child(userId).child("quizzesTaken").get().await()
        return snapshot.getValue(Int::class.java) ?: 0
    }

    suspend fun saveQuizAttempt(userId: String, score: Int) {
        val timestamp = System.currentTimeMillis()
        db.child(userId).child("history").child(timestamp.toString()).setValue(score).await()
    }

    suspend fun getQuizHistory(userId: String): List<Pair<String, Int>> {
        val snapshot = db.child(userId).child("history").get().await()
        val list = mutableListOf<Pair<String, Int>>()
        snapshot.children.forEach { child ->
            val time = child.key?.toLongOrNull() ?: return@forEach
            val score = child.getValue(Int::class.java) ?: return@forEach
            val date = java.text.SimpleDateFormat(
                "dd-MM-yyyy HH:mm",
                java.util.Locale.getDefault()
            ).format(java.util.Date(time))
            list.add(date to score)
        }
        return list.sortedByDescending { it.first }
    }
}
