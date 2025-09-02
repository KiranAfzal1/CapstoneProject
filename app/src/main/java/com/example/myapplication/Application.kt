package com.example.myapplication

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Application : Application() {
    override fun onCreate() {
        super.onCreate()

        val auth = FirebaseAuth.getInstance()
        val db = FirebaseDatabase.getInstance().reference

        auth.addAuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {

                db.child("users").child(user.uid).child("status")
                    .onDisconnect().setValue("offline")

                db.child("users").child(user.uid).child("status")
                    .setValue("online")
            }
        }
    }
}
