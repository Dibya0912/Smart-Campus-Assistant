package com.example.project

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.project.databinding.ActivityFacultyLoginSignupBinding
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.FirebaseFirestore

class FacultyLoginSignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFacultyLoginSignupBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var isLogin = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFacultyLoginSignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        db = FirebaseFirestore.getInstance()
        setupTabs()

        binding.buttonAction.setOnClickListener {
            if (isLogin) loginUser()
            else signUpUser()
        }
    }

    private fun setupTabs() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 0) {
                    isLogin = true
                    binding.nameLayout.visibility = View.GONE
                    binding.confirmPasswordLayout.visibility = View.GONE
                    binding.buttonAction.text = "Login"
                } else {
                    isLogin = false
                    binding.nameLayout.visibility = View.VISIBLE
                    binding.confirmPasswordLayout.visibility = View.VISIBLE
                    binding.buttonAction.text = "Sign Up"
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun signUpUser() {
        val name = binding.nameEditText.text.toString().trim()
        val email = binding.emailEditText.text.toString().trim().lowercase()
        val password = binding.passwordEditText.text.toString()
        val confirmPassword = binding.confirmPasswordEditText.text.toString()

        if (name.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Only faculty domain allowed (allow gmail for testing if you want)
        if (!email.endsWith("@faculty.yourcollege.com") && !email.endsWith("@gmail.com")) {
            Toast.makeText(this, "Please use a valid faculty email", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        binding.progressBar.visibility = View.VISIBLE
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                binding.progressBar.visibility = View.GONE
                if (task.isSuccessful) {
                    // Save faculty profile to Firestore
                    val uid = auth.currentUser?.uid ?: ""
                    val userMap = hashMapOf(
                        "uid" to uid,
                        "name" to name,
                        "email" to email,
                        "role" to "faculty",
                        "password" to password
                    )

                    db.collection("users").document(uid)
                        .set(userMap)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Sign up successful!", Toast.LENGTH_SHORT).show()
                            navigateToDashboard()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Signed up but failed to save profile: ${e.message}", Toast.LENGTH_LONG).show()
                            navigateToDashboard()
                        }
                } else {
                    Toast.makeText(this, "Sign up failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun loginUser() {
        val email = binding.emailEditText.text.toString().trim().lowercase()
        val password = binding.passwordEditText.text.toString()

        if (email.isBlank() || password.isBlank()) {
            Toast.makeText(this, "Email and password cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        // Faculty domain check
        if (!email.endsWith("@faculty.yourcollege.com") && !email.endsWith("@gmail.com")) {
            Toast.makeText(this, "Access denied! Please log in using your Faculty email.", Toast.LENGTH_LONG).show()
            return
        }

        binding.progressBar.visibility = View.VISIBLE
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                binding.progressBar.visibility = View.GONE
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                    navigateToDashboard()
                } else {
                    Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun navigateToDashboard() {
        val intent = Intent(this, FacultyDashboardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
