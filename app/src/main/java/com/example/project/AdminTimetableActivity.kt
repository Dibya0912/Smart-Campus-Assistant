package com.example.project

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class AdminTimetableActivity : AppCompatActivity() {

    private val database = FirebaseDatabase.getInstance().getReference("timetable")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_timetable)

        val sectionSpinner = findViewById<Spinner>(R.id.spinnerSection)
        val daySpinner = findViewById<Spinner>(R.id.spinnerDay)
        val timeSlotSpinner = findViewById<Spinner>(R.id.spinnerTimeSlot)
        val subjectSpinner = findViewById<Spinner>(R.id.spinnerSubject)
        val facultySpinner = findViewById<Spinner>(R.id.spinnerFaculty)
        val saveButton = findViewById<Button>(R.id.btnSave)

        val sections = arrayOf("Section A", "Section B")
        val days = arrayOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
        val timeSlots = arrayOf(
            "9_30_10_30", "10_30_11_30", "11_30_12_30",
            "12_30_1_30", "1_30_2_30", "2_30_3_30"
        )
        val subjects = arrayOf(
            "Angular", "Spring Boot", "Android App Development",
            "Advanced Java", "Job Readiness / Soft Skills", "Free", "Lunch"
        )
        val faculty = arrayOf(
            "Prof. A Sharma", "Prof. B Gupta", "Prof. C Mehta",
            "Prof. D Patel", "Prof. E Roy"
        )

        sectionSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sections)
        daySpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, days)
        timeSlotSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, timeSlots)
        subjectSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, subjects)
        facultySpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, faculty)

        saveButton.setOnClickListener {
            val section = if (sectionSpinner.selectedItem == "Section A") "sectionA" else "sectionB"
            val day = daySpinner.selectedItem.toString().lowercase()
            val slot = timeSlotSpinner.selectedItem.toString()
            val subject = subjectSpinner.selectedItem.toString()
            val facultyName = facultySpinner.selectedItem.toString()
            val entryValue = if (subject == "Free" || subject == "Lunch") subject else "$subject - $facultyName"

            database.child(section).child(day).child(slot).setValue(entryValue)
                .addOnSuccessListener {
                    Toast.makeText(this, "✅ Timetable updated successfully!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "❌ Error: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
