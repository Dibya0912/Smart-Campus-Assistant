package com.example.project

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.example.project.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Animate gradient background
        val animationDrawable = binding.view.background as? AnimationDrawable
        animationDrawable?.setEnterFadeDuration(1500)
        animationDrawable?.setExitFadeDuration(1500)
        animationDrawable?.start()

        // Handle clicks
        binding.cardFaculty.setOnClickListener {
            startActivity(Intent(this, FacultyLoginSignupActivity::class.java))
        }

        binding.cardStudent.setOnClickListener {
            startActivity(Intent(this, StudentLoginSignupActivity::class.java))
        }

        binding.cardAdmin.setOnClickListener {
            startActivity(Intent(this, AdminLoginActivity::class.java))
        }
    }
}
