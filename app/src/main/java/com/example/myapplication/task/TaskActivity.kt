package com.example.myapplication.task

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.adapter.TaskAdapter
import com.example.myapplication.databinding.ActivityTaskBinding
import com.example.myapplication.model.Task
import com.example.myapplication.viewmodel.TaskViewModel
import com.google.android.material.tabs.TabLayout
import com.example.myapplication.SharedPrefManager
import com.example.myapplication.launch.LoginActivity

class TaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskBinding
    private val viewModel: TaskViewModel by viewModels()
    private lateinit var sharedPref: SharedPrefManager

    private lateinit var pendingAdapter: TaskAdapter
    private lateinit var completedAdapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskBinding.inflate(layoutInflater)
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

        sharedPref = SharedPrefManager(this)

        if (sharedPref.getUserId() == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        showLoading(true)
        setupAdapters()
        setupRecyclerViews()
        setupObservers()
        setupFab()
        setupTabs()

        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    private fun setupAdapters() {
        pendingAdapter = TaskAdapter(emptyList(),
            onEdit = { openAddEditTask(it) },
            onDelete = { viewModel.deleteTask(it) },
            onComplete = { viewModel.markTaskCompleted(it) }
        )

        completedAdapter = TaskAdapter(emptyList(),
            onEdit = { openAddEditTask(it) },
            onDelete = { viewModel.deleteTask(it) },
            onComplete = { }
        )
    }

    private fun setupRecyclerViews() {
        binding.recyclerViewPending.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewPending.adapter = pendingAdapter

        binding.recyclerViewCompleted.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewCompleted.adapter = completedAdapter
    }

    private fun setupObservers() {
        viewModel.loading.observe(this) { showLoading(it) }
        viewModel.pendingTasks.observe(this) { pendingAdapter.updateTasks(it) }
        viewModel.completedTasks.observe(this) { completedAdapter.updateTasks(it) }
    }

    private fun setupFab() {
        binding.fabAddTask.setOnClickListener { openAddEditTask(null) }
    }

    private fun setupTabs() {
        binding.taskTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                toggleRecyclerViews(tab.position == 0)
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun toggleRecyclerViews(showPending: Boolean) {
        binding.recyclerViewPending.visibility = if (showPending) View.VISIBLE else View.GONE
        binding.recyclerViewCompleted.visibility = if (showPending) View.GONE else View.VISIBLE
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
        if (show) {
            binding.recyclerViewPending.visibility = View.GONE
            binding.recyclerViewCompleted.visibility = View.GONE
        }
    }

    private fun openAddEditTask(task: Task?) {
        val fragment = AddEditTaskFragment().apply {
            arguments = Bundle().apply {
                putString("MODE", if (task == null) "ADD" else "EDIT")
                putParcelable("TASK_OBJECT", task)
            }
        }
        fragment.show(supportFragmentManager, "AddEditTaskFragment")
    }


}
