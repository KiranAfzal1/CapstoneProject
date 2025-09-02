package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityProfileBinding
import com.google.firebase.database.FirebaseDatabase

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private val db = FirebaseDatabase.getInstance().reference

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

        val userId = intent.getStringExtra("userId") ?: return
        loadUser(userId)

    }

    private fun loadUser(userId: String) {
        db.child("users").child(userId).get().addOnSuccessListener { snapshot ->
            binding.tvUserName.text = snapshot.child("name").getValue(String::class.java) ?: "User"
            binding.tvUserEmail.text = snapshot.child("email").getValue(String::class.java) ?: ""
            binding.ivProfile.setImageResource(R.drawable.ic_person)
        }

        db.child("users").child(userId).child("status")
            .addValueEventListener(object : com.google.firebase.database.ValueEventListener {
                override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                    val status = snapshot.getValue(String::class.java) ?: "offline"
                    binding.tvUserStatus.text = status
                    binding.tvUserStatus.setTextColor(
                        if (status == "online") getColor(android.R.color.holo_green_dark)
                        else getColor(android.R.color.holo_red_dark)
                    )
                }

                override fun onCancelled(error: com.google.firebase.database.DatabaseError) {}
            })
    }

}
