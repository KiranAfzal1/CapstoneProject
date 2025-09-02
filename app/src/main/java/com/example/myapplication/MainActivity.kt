package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.myapplication.chat.ChatListActivity
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.launch.LoginActivity
import com.example.myapplication.quiz.QuizHomeActivity
import com.example.myapplication.task.TaskActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPrefManager: SharedPrefManager
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance().getReference("users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
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

        if (sharedPrefManager.getUserId() == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        loadUserProfile()

        binding.btnChat.setOnClickListener { openActivity(ChatListActivity::class.java) }
        binding.btnQuiz.setOnClickListener { openActivity(QuizHomeActivity::class.java) }
        binding.btnTask.setOnClickListener { openActivity(TaskActivity::class.java) }
        binding.profileCard.setOnClickListener { openActivity(SettingsActivity::class.java) }
    }

    private fun openActivity(target: Class<*>) {
        val userId = sharedPrefManager.getUserId()
        if (userId != null) {
            startActivity(Intent(this, target))
        } else {
            Toast.makeText(this, "You must be logged in to access this section", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun loadUserProfile() {
        val userId = sharedPrefManager.getUserId() ?: return

        db.child(userId).get().addOnSuccessListener { snapshot ->
            val name = snapshot.child("name").getValue(String::class.java) ?: "User Name"
            val email = snapshot.child("email").getValue(String::class.java) ?: auth.currentUser?.email ?: "user@example.com"
            val profileUrl = snapshot.child("profileImage").getValue(String::class.java) // 👈 now using URL

            binding.nameText.text = name
            binding.emailText.text = email

            if (!profileUrl.isNullOrEmpty()) {
                Glide.with(this)
                    .load(profileUrl)
                    .placeholder(R.drawable.ic_person)
                    .error(R.drawable.ic_person)
                    .into(binding.profilePic)
            } else {
                binding.profilePic.setImageResource(R.drawable.ic_person)
            }

        }.addOnFailureListener {
            binding.nameText.text = auth.currentUser?.displayName ?: "User Name"
            binding.emailText.text = auth.currentUser?.email ?: "user@example.com"
            binding.profilePic.setImageResource(R.drawable.ic_person)
        }
    }
}
