package com.example.myapplication.repository

import com.example.myapplication.model.Message
import com.google.firebase.database.FirebaseDatabase

class ChatRepository {

    private val db = FirebaseDatabase
        .getInstance("https://socialapp-d9381-default-rtdb.asia-southeast1.firebasedatabase.app")
        .reference

    fun getGroupsRef() = db.child("chats")

    fun getMessagesRef(groupName: String) = db.child("chats").child(groupName).child("messages")

    fun sendMessage(groupName: String, message: Message, onComplete: (Boolean) -> Unit = {}) {
        val messageRef = getMessagesRef(groupName).push()
        val generatedId = messageRef.key ?: return onComplete(false)
        val timestamp = System.currentTimeMillis()

        val msgMap = mapOf(
            "id" to generatedId,
            "senderId" to message.senderId,
            "senderName" to message.senderName,
            "senderProfileUrl" to message.senderProfileUrl,
            "message" to message.message,
            "timestamp" to timestamp
        )

        messageRef.setValue(msgMap)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }

        val metadata = mapOf(
            "id" to groupName,
            "name" to groupName,
            "lastMessage" to message.message,
            "lastMessageTime" to timestamp
        )
        getGroupsRef().child(groupName).child("metadata").updateChildren(metadata)
    }

    fun getUsersRef() = db.child("users")
}
