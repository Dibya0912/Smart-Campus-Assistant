package com.example.project

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.project.databinding.ActivityAdminLoginBinding

class AdminLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Hardcoded admin credentials (can later move to Firestore or shared prefs)
        val adminEmail = "admin@university.com"
        val adminPassword = "admin123"

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.progressBar.visibility = View.VISIBLE

            // Simulate login check
            if (email.equals(adminEmail, ignoreCase = true) && password == adminPassword) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()

                // Navigate to admin dashboard
                val intent = Intent(this, AdminDashboardActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, "Invalid admin credentials", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
