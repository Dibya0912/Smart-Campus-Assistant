package com.example.project

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView

class AdminDashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        // Find dashboard cards
        val manageTimetableCard = findViewById<MaterialCardView>(R.id.manageTimetableCard)
        val manageEventsCard = findViewById<MaterialCardView>(R.id.manageEventsCard)
        val managePapersCard = findViewById<MaterialCardView>(R.id.managePapersCard)
        val updateCrContactCard = findViewById<MaterialCardView>(R.id.updateCrContactCard)
        val updateFacultyContactCard = findViewById<MaterialCardView>(R.id.updateFacultyContactCard)
        val logoutButton = findViewById<Button>(R.id.logoutButton)

        // 📘 Manage Timetable
        manageTimetableCard.setOnClickListener {
            Toast.makeText(this, "Opening Timetable Management...", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, AdminTimetableActivity::class.java))
        }

        // 📅 Manage Events
        manageEventsCard.setOnClickListener {
            Toast.makeText(this, "Opening Events Management...", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, AdminEventActivity::class.java))
        }

        // 📄 Manage Question Papers
        managePapersCard.setOnClickListener {
            Toast.makeText(this, "Opening Question Paper Management...", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, AdminModulePaperActivity::class.java))
        }

        // ✅ Update CR Contact
        updateCrContactCard.setOnClickListener {
            Toast.makeText(this, "Opening Update CR Contact...", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, UpdateCrContactActivity::class.java))
        }

        // ✅ Update Faculty Contact (now connected)
        updateFacultyContactCard.setOnClickListener {
            Toast.makeText(this, "Opening Update Faculty Contact...", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, UpdateFacultyContactActivity::class.java))
        }

        // 🚪 Logout
        logoutButton.setOnClickListener {
            Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
