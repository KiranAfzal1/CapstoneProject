package com.example.myapplication.chat

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.ProfileActivity
import com.example.myapplication.SharedPrefManager
import com.example.myapplication.adapter.MessageAdapter
import com.example.myapplication.databinding.ActivityChatBinding
import com.example.myapplication.model.Message
import com.example.myapplication.viewmodel.ChatViewModel
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private val viewModel: ChatViewModel by viewModels()
    private lateinit var adapter: MessageAdapter
    private lateinit var sharedPrefManager: SharedPrefManager
    private val dbRef = FirebaseDatabase
        .getInstance("https://socialapp-d9381-default-rtdb.asia-southeast1.firebasedatabase.app")
        .reference

    private val groupName: String by lazy {
        intent.getStringExtra("GROUP_NAME") ?: "default"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
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

        adapter = MessageAdapter(mutableListOf()) { senderId ->

            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("userId", senderId)
            startActivity(intent)
        }
        binding.messagesRecyclerView.layoutManager = LinearLayoutManager(this).apply {
            stackFromEnd = true
        }
        binding.messagesRecyclerView.setHasFixedSize(true)
        binding.messagesRecyclerView.itemAnimator = null
        binding.messagesRecyclerView.adapter = adapter


        viewModel.observeMessages(groupName)


        viewModel.messages.observe(this) { msgs ->
            adapter.updateMessages(msgs)
            if (msgs.isNotEmpty()) {
                binding.messagesRecyclerView.scrollToPosition(msgs.size - 1)
            }
        }

        binding.sendButton.setOnClickListener {
            val text = binding.messageEditText.text.toString().trim()
            if (text.isNotEmpty()) {
                val userId = sharedPrefManager.getUserId()

                if (!userId.isNullOrEmpty()) {

                    dbRef.child("users").child(userId).child("name")
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val userName = snapshot.getValue(String::class.java) ?: "User"

                                val message = Message(
                                    id = "",
                                    senderId = userId,
                                    senderName = userName,
                                    senderProfileUrl = null,
                                    message = text,
                                    timestamp = System.currentTimeMillis()
                                )

                                viewModel.sendMessage(groupName, message) { success ->
                                    if (success) {
                                        binding.messageEditText.text.clear()
                                    } else {
                                        Toast.makeText(
                                            this@ChatActivity,
                                            "Failed to send",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(
                                    this@ChatActivity,
                                    "Error fetching user name",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })
                } else {
                    Toast.makeText(this, "Not signed in", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
