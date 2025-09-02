package com.example.myapplication.model

data class Message(
    val id: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val senderProfileUrl: String? = null,
    val message: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
