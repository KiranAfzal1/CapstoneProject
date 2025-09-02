package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.model.Task
import com.example.myapplication.repository.TaskRepository
import com.google.firebase.database.*

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = TaskRepository(application)

    private val _pendingTasks = MutableLiveData<List<Task>>()
    val pendingTasks: LiveData<List<Task>> get() = _pendingTasks

    private val _completedTasks = MutableLiveData<List<Task>>()
    val completedTasks: LiveData<List<Task>> get() = _completedTasks

    private val _loading = MutableLiveData(true)
    val loading: LiveData<Boolean> get() = _loading

    private var tasksListener: ValueEventListener? = null
    private var listenerAttached = false

    init {
        attachTasksListener()
    }

    private fun attachTasksListener() {
        if (listenerAttached) return
        val tasksRef = repository.getTasksRef() ?: run {
            _loading.value = false
            return
        }

        tasksListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val allTasks = snapshot.children.mapNotNull { it.getValue(Task::class.java) }
                _pendingTasks.value = allTasks.filter { !it.isCompleted }
                _completedTasks.value = allTasks.filter { it.isCompleted }
                _loading.value = false
            }

            override fun onCancelled(error: DatabaseError) {
                _loading.value = false
            }
        }
        tasksRef.addValueEventListener(tasksListener as ValueEventListener)
        listenerAttached = true
    }

    private fun detachTasksListener() {
        tasksListener?.let { repository.getTasksRef()?.removeEventListener(it) }
        tasksListener = null
        listenerAttached = false
    }

    override fun onCleared() {
        super.onCleared()
        detachTasksListener()
    }

    fun addOrUpdateTask(task: Task) = repository.addOrUpdateTask(task)
    fun deleteTask(task: Task) = repository.deleteTask(task)
    fun markTaskCompleted(task: Task) = repository.markTaskCompleted(task)
}
