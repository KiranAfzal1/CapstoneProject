package com.example.myapplication.repository

import android.content.Context
import com.example.myapplication.SharedPrefManager
import com.example.myapplication.model.Task
import com.google.firebase.database.*

class TaskRepository(context: Context) {

    private val sharedPref = SharedPrefManager(context)
    private val dbUrl =
        "https://socialapp-d9381-default-rtdb.asia-southeast1.firebasedatabase.app"
    private val db: DatabaseReference = FirebaseDatabase.getInstance(dbUrl).reference


    private fun getCurrentUserId(): String? = sharedPref.getUserId()


    private fun userRef(): DatabaseReference? =
        getCurrentUserId()?.let { db.child("users").child(it) }

    fun getTasksRef(): DatabaseReference? = userRef()?.child("tasks")

    fun addOrUpdateTask(task: Task) {
        val tasksRef = getTasksRef() ?: return
        if (task.id.isEmpty()) {
            task.id = tasksRef.push().key ?: return
            incrementCounter("totalTasks", 1)
        }
        tasksRef.child(task.id).setValue(task)
    }

    fun deleteTask(task: Task) {
        val tasksRef = getTasksRef() ?: return
        tasksRef.child(task.id).removeValue().addOnSuccessListener {
            decrementCounter("totalTasks", 1)
            if (task.isCompleted) decrementCounter("tasksCompleted", 1)
        }
    }

    fun markTaskCompleted(task: Task) {
        val tasksRef = getTasksRef() ?: return
        if (!task.isCompleted) {
            task.isCompleted = true
            tasksRef.child(task.id).setValue(task).addOnSuccessListener {
                incrementCounter("tasksCompleted", 1)
            }
        }
    }

    private fun incrementCounter(field: String, value: Int) {
        userRef()?.child(field)?.runTransaction(object : Transaction.Handler {
            override fun doTransaction(currentData: MutableData): Transaction.Result {
                val current = currentData.getValue(Int::class.java) ?: 0
                currentData.value = current + value
                return Transaction.success(currentData)
            }

            override fun onComplete(
                error: DatabaseError?,
                committed: Boolean,
                currentData: DataSnapshot?
            ) {}
        })
    }

    private fun decrementCounter(field: String, value: Int) {
        userRef()?.child(field)?.runTransaction(object : Transaction.Handler {
            override fun doTransaction(currentData: MutableData): Transaction.Result {
                val current = currentData.getValue(Int::class.java) ?: 0
                currentData.value = (current - value).coerceAtLeast(0)
                return Transaction.success(currentData)
            }

            override fun onComplete(
                error: DatabaseError?,
                committed: Boolean,
                currentData: DataSnapshot?
            ) {}
        })
    }
}
