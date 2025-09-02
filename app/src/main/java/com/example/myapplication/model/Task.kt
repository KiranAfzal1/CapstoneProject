package com.example.myapplication.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Task(
    var id: String = "",
    var title: String = "",
    var description: String = "",
    var priority: String = "",
    var dueDate: String? = null,
    var isCompleted: Boolean = false
) : Parcelable
