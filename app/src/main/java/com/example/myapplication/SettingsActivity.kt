package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.FragmentSettingsBinding
import com.example.myapplication.launch.LoginActivity
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var sharedPrefManager: SharedPrefManager
    private val db = FirebaseDatabase.getInstance(
        "https://socialapp-d9381-default-rtdb.asia-southeast1.firebasedatabase.app"
    ).getReference("users")

    private var userId: String? = null
    private val PICK_IMAGE_REQUEST = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentSettingsBinding.inflate(layoutInflater)
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
        userId = sharedPrefManager.getUserId()

        if (userId == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        fetchUserData()

        binding.profileImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        binding.editNameIcon.setOnClickListener { showEditNameDialog() }

        binding.logoutButton.setOnClickListener {
            sharedPrefManager.clear()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun fetchUserData() {
        val uid = userId ?: return
        db.child(uid).get().addOnSuccessListener { snapshot ->
            val name = snapshot.child("name").getValue(String::class.java) ?: "User Name"
            val email = snapshot.child("email").getValue(String::class.java) ?: "No Email"
            val quizzesTaken = snapshot.child("quizzesTaken").getValue(Int::class.java) ?: 0
            val bestScore = snapshot.child("bestScore").getValue(Int::class.java) ?: 0
            val tasksCompleted = snapshot.child("tasksCompleted").getValue(Int::class.java) ?: 0
            val totalTasks = snapshot.child("totalTasks").getValue(Int::class.java) ?: 0
            val profileBase64 = snapshot.child("profileImage").getValue(String::class.java)

            binding.nameText.text = name
            binding.emailText.text = email
            binding.quizCountText.text = "Quizzes Taken: $quizzesTaken"
            binding.bestScoreText.text = "Best Score: $bestScore/5"
            binding.taskCountText.text = "Tasks: $tasksCompleted/$totalTasks completed"

            if (!profileBase64.isNullOrEmpty()) {
                try {
                    val bytes = Base64.decode(profileBase64, Base64.DEFAULT)
                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    Glide.with(this)
                        .load(bitmap)
                        .placeholder(R.drawable.ic_person)
                        .into(binding.profileImage)
                } catch (e: Exception) {
                    binding.profileImage.setImageResource(R.drawable.ic_person)
                }
            } else {
                binding.profileImage.setImageResource(R.drawable.ic_person)
            }
        }
    }

    private fun saveImageAsBase64(imageUri: Uri) {
        val uid = userId ?: return
        val inputStream = contentResolver.openInputStream(imageUri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()

        if (bitmap != null) {
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos)
            val bytes = baos.toByteArray()
            val base64String = Base64.encodeToString(bytes, Base64.DEFAULT)

            db.child(uid).child("profileImage").setValue(base64String)
                .addOnSuccessListener {
                    Glide.with(this).load(bitmap).into(binding.profileImage)
                    Toast.makeText(this, "Profile picture updated!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Upload failed: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Failed to decode image!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showEditNameDialog() {
        val input = EditText(this)
        input.hint = "Enter new name"
        AlertDialog.Builder(this)
            .setTitle("Edit Username")
            .setView(input)
            .setPositiveButton("Save") { dialog, _ ->
                val newName = input.text.toString().trim()
                if (newName.isNotEmpty()) updateUsername(newName)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun updateUsername(newName: String) {
        val uid = userId ?: return
        db.child(uid).child("name").setValue(newName).addOnSuccessListener {
            binding.nameText.text = newName
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.data ?: return
            saveImageAsBase64(imageUri)
        }
    }
}
