package com.example.myapplication

import android.app.Application
import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val pref = SharedPrefManager(this)
        val userId = pref.getUserId()

        if (userId != null) {
            val db = FirebaseDatabase.getInstance(
                "https://socialapp-d9381-default-rtdb.asia-southeast1.firebasedatabase.app"
            ).reference

            val userRef = db.child("users").child(userId)

            val offlineStatus = mapOf(
                "status" to "offline",
                "last_seen" to ServerValue.TIMESTAMP
            )
            userRef.onDisconnect().updateChildren(offlineStatus)
                .addOnSuccessListener {
                    Log.d("MyApplication", "onDisconnect set successfully")
                }.addOnFailureListener { e ->
                    Log.e("MyApplication", "onDisconnect failed: ${e.message}")
                }

            userRef.updateChildren(mapOf(
                "status" to "online",
                "last_seen" to ServerValue.TIMESTAMP
            ))
            Log.d("MyApplication", "User $userId set online")
        } else {
            Log.d("MyApplication", "No userId in SharedPref — cannot track presence")
        }
    }
}
