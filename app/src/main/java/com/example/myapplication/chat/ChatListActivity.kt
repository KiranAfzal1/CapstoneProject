package com.example.myapplication.chat

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.adapter.ChatGroupAdapter
import com.example.myapplication.databinding.ActivityChatListBinding
import com.example.myapplication.model.ChatGroup
import com.example.myapplication.repository.ChatRepository
import com.google.firebase.database.*

class ChatListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatListBinding
    private lateinit var adapter: ChatGroupAdapter
    private val repository = ChatRepository()
    private val groups = mutableListOf<ChatGroup>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatListBinding.inflate(layoutInflater)
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

        setupRecyclerView()
        fetchGroups()
        
        binding.ivBack.setOnClickListener {
            finish()
        }

    }

    private fun setupRecyclerView() {
        adapter = ChatGroupAdapter(groups) { group ->
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("GROUP_NAME", group.name)
            startActivity(intent)
        }
        binding.rvChatList.layoutManager = LinearLayoutManager(this)
        binding.rvChatList.adapter = adapter
    }
    private fun fetchGroups() {
        repository.getGroupsRef().addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                groups.clear()
                snapshot.children.forEach { child ->
                    val groupName = child.key ?: return@forEach
                    groups.add(ChatGroup(groupName, groupName, "", 0))
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}
