package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityProfileBinding
import com.google.firebase.database.*

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private val db = FirebaseDatabase.getInstance(
        "https://socialapp-d9381-default-rtdb.asia-southeast1.firebasedatabase.app"
    ).reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
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

        val userIdFromIntent = intent.getStringExtra("userId")

        val pref = SharedPrefManager(this)
        val userId = userIdFromIntent ?: pref.getUserId()

        if (userId == null) {
            binding.tvUserStatus.text = "User not logged in"
            return
        }

        loadUser(userId)
    }


    private fun loadUser(userId: String) {
        val userRef = db.child("users").child(userId)

        userRef.get().addOnSuccessListener { snapshot ->
            binding.tvUserName.text = snapshot.child("name").getValue(String::class.java) ?: "User"
            binding.tvUserEmail.text = snapshot.child("email").getValue(String::class.java) ?: ""
            binding.ivProfile.setImageResource(R.drawable.ic_person)
        }

        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val status = snapshot.child("status").getValue(String::class.java) ?: "offline"
                val lastSeenVal = snapshot.child("last_seen").value
                val lastSeen: Long = when (lastSeenVal) {
                    is Long -> lastSeenVal
                    is Double -> lastSeenVal.toLong()
                    else -> 0L
                }

                if (status == "online") {
                    binding.tvUserStatus.text = "Online"
                    binding.tvUserStatus.setTextColor(getColor(android.R.color.holo_green_dark))
                } else {
                    binding.tvUserStatus.text =
                        if (lastSeen > 0) formatLastSeen(lastSeen) else "Offline"
                    binding.tvUserStatus.setTextColor(getColor(android.R.color.holo_red_dark))
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun formatLastSeen(ts: Long): String {
        val sdf = java.text.SimpleDateFormat(
            "dd MMM yyyy, hh:mm a",
            java.util.Locale.getDefault()
        )
        return "Last seen: ${sdf.format(java.util.Date(ts))}"
    }
}
