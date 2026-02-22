package com.example.project

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class UpdateFacultyContactActivity : AppCompatActivity() {

    private val dbRef = FirebaseDatabase.getInstance().getReference("college_data/faculty_contacts")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_faculty_contact)

        val nameFields = listOf(
            findViewById<EditText>(R.id.etFaculty1Name),
            findViewById<EditText>(R.id.etFaculty2Name),
            findViewById<EditText>(R.id.etFaculty3Name),
            findViewById<EditText>(R.id.etFaculty4Name),
            findViewById<EditText>(R.id.etFaculty5Name)
        )
        val phoneFields = listOf(
            findViewById<EditText>(R.id.etFaculty1Phone),
            findViewById<EditText>(R.id.etFaculty2Phone),
            findViewById<EditText>(R.id.etFaculty3Phone),
            findViewById<EditText>(R.id.etFaculty4Phone),
            findViewById<EditText>(R.id.etFaculty5Phone)
        )

        val btnSave = findViewById<Button>(R.id.btnSaveFaculty)

        btnSave.setOnClickListener {
            val facultyList = mutableListOf<Map<String, String>>()
            for (i in 0 until nameFields.size) {
                val name = nameFields[i].text.toString().trim()
                val phone = phoneFields[i].text.toString().trim()
                if (name.isNotEmpty() && phone.isNotEmpty()) {
                    facultyList.add(mapOf("name" to name, "phone" to phone))
                }
            }

            if (facultyList.isEmpty()) {
                Toast.makeText(this, "Please fill at least one faculty contact", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Save the list to Firebase
            dbRef.setValue(facultyList)
                .addOnSuccessListener {
                    Toast.makeText(this, "✅ Faculty contacts updated successfully!", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "❌ Failed to update: ${it.message}", Toast.LENGTH_LONG).show()
                }
        }
    }
}
