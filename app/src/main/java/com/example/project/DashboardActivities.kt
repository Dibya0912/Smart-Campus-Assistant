package com.example.project

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.project.databinding.ActivityFacultyDashboardBinding
import com.example.project.databinding.ActivityStudentDashboardBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

// -------------------- FACULTY DASHBOARD --------------------
class FacultyDashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFacultyDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFacultyDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // --- Display faculty details ---
        val user = Firebase.auth.currentUser
        binding.welcomeTextView.text = "Welcome, Faculty!"
        binding.emailTextView.text = user?.email

        // --- Navigation Buttons ---
        binding.cardStudentTimetable.setOnClickListener {
            startActivity(Intent(this, StudentTimetableActivity::class.java))
        }

        binding.cardFacultyTimetable.setOnClickListener {
            startActivity(Intent(this, FacultyTimetableActivity::class.java))
        }

        // ✅ CR Contacts (for Faculty)
        binding.cardCRContact.setOnClickListener {
            startActivity(Intent(this, FacultyContactsActivity::class.java))
        }

        // --- Logout ---
        binding.logoutButton.setOnClickListener {
            Firebase.auth.signOut()
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}

// -------------------- STUDENT DASHBOARD --------------------
class StudentDashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStudentDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // --- Display student details ---
        val user = Firebase.auth.currentUser
        binding.welcomeTextView.text = "Welcome, Student!"
        binding.emailTextView.text = user?.email

        // --- Navigation Buttons ---
        binding.cardStudentTimetable.setOnClickListener {
            startActivity(Intent(this, StudentTimetableActivity::class.java))
        }

        binding.cardFacultyTimetable.setOnClickListener {
            startActivity(Intent(this, FacultyTimetableActivity::class.java))
        }

        //  Faculty Contacts
        binding.cardFacultyContacts.setOnClickListener {
            startActivity(Intent(this, StudentFacultyContactsActivity::class.java))
        }

        //  Opens subject list for question papers
        binding.cardModulePapers.setOnClickListener {
            startActivity(Intent(this, StudentSubjectListActivity::class.java))
        }

        binding.cardEventReminder.setOnClickListener {
            startActivity(Intent(this, EventReminderActivity::class.java))
        }

        // --- Logout ---
        binding.logoutButton.setOnClickListener {
            Firebase.auth.signOut()
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}
