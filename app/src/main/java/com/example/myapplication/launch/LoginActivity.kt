package com.example.myapplication.launch

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.HomeActivity
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.SharedPrefManager
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.*

class LoginActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: MaterialButton
    private lateinit var registerLink: TextView
    private lateinit var database: FirebaseDatabase
    private lateinit var usersRef: DatabaseReference
    private lateinit var sharedPrefManager: SharedPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.statusBarColor = Color.WHITE
            window.navigationBarColor = Color.WHITE
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or
                        View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        } else {
            window.statusBarColor = Color.BLACK
            window.navigationBarColor = Color.BLACK
        }

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        registerLink = findViewById(R.id.registerLink)

        sharedPrefManager = SharedPrefManager(this)

        if (sharedPrefManager.isLoggedIn()) {
            val targetActivity = if (sharedPrefManager.isFirstLogin()) HomeActivity::class.java else MainActivity::class.java
            sharedPrefManager.setFirstLoginDone()
            startActivity(Intent(this, targetActivity))
            finish()
        }

        database = FirebaseDatabase.getInstance(
            "https://socialapp-d9381-default-rtdb.asia-southeast1.firebasedatabase.app"
        )
        usersRef = database.getReference("users")

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            } else {
                loginUser(email, password)
            }
        }

        registerLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun loginUser(email: String, password: String) {
        usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var found = false
                var userId: String? = null
                for (userSnap in snapshot.children) {
                    val userMap = userSnap.value as? Map<*, *>
                    val storedEmail = userMap?.get("email") as? String
                    val storedPassword = userMap?.get("password") as? String
                    if (storedEmail == email && storedPassword == password) {
                        found = true
                        userId = userSnap.key
                        break
                    }
                }

                if (found && userId != null) {

                    sharedPrefManager.setLogin(true, userId)

                    val targetActivity = if (sharedPrefManager.isFirstLogin()) HomeActivity::class.java else MainActivity::class.java
                    sharedPrefManager.setFirstLoginDone()
                    startActivity(Intent(this@LoginActivity, targetActivity))
                    Toast.makeText(this@LoginActivity, "Login Successful!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, "Invalid email or password", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@LoginActivity, "Database Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
