package com.example.myapplication.model

data class ChatGroup(
    val id: String = "",
    val name: String = "",
    val lastMessage: String = "",
    val lastMessageTime: Long = System.currentTimeMillis()
)
