package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.model.Message
import com.example.myapplication.repository.ChatRepository
import com.google.firebase.database.*

class ChatViewModel : ViewModel() {

    private val repository = ChatRepository()
    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> get() = _messages

    private var messagesListener: ValueEventListener? = null
    private var currentGroup: String? = null

    fun observeMessages(groupName: String) {
        messagesListener?.let {
            currentGroup?.let { oldGroup ->
                repository.getMessagesRef(oldGroup).removeEventListener(it)
            }
        }

        currentGroup = groupName
        messagesListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Message>()
                snapshot.children.forEach { msgSnap ->
                    val id = msgSnap.key ?: ""
                    val senderId = msgSnap.child("senderId").getValue(String::class.java) ?: ""
                    val senderName = msgSnap.child("senderName").getValue(String::class.java) ?: "User"
                    val senderProfileUrl = msgSnap.child("senderProfileUrl").getValue(String::class.java)
                    val text = msgSnap.child("message").getValue(String::class.java) ?: ""
                    val timestamp = msgSnap.child("timestamp").getValue(Long::class.java) ?: System.currentTimeMillis()

                    list.add(Message(id, senderId, senderName, senderProfileUrl, text, timestamp))
                }
                _messages.value = list.sortedBy { it.timestamp }
            }

            override fun onCancelled(error: DatabaseError) {}
        }
        repository.getMessagesRef(groupName).addValueEventListener(messagesListener!!)
    }

    fun sendMessage(groupName: String, message: Message, onComplete: (Boolean) -> Unit = {}) {
        repository.sendMessage(groupName, message, onComplete)
    }

    override fun onCleared() {
        super.onCleared()
        messagesListener?.let { listener ->
            currentGroup?.let { repository.getMessagesRef(it).removeEventListener(listener) }
        }
    }
}

