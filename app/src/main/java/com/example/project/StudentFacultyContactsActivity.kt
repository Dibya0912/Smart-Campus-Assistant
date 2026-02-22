package com.example.project

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView
import com.google.firebase.database.*

class StudentFacultyContactsActivity : AppCompatActivity() {

    private val dbRef =
        FirebaseDatabase.getInstance().getReference("college_data/faculty_contacts")

    private lateinit var progressBar: ProgressBar
    private lateinit var facultyContainer: LinearLayout
    private lateinit var tvNoData: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_faculty_contacts)

        progressBar = findViewById(R.id.progressBar)
        facultyContainer = findViewById(R.id.facultyContainer)
        tvNoData = findViewById(R.id.tvNoData)

        loadFacultyContacts()
    }

    private fun loadFacultyContacts() {
        progressBar.visibility = View.VISIBLE
        tvNoData.visibility = View.GONE
        facultyContainer.removeAllViews()

        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                progressBar.visibility = View.GONE

                if (!snapshot.exists()) {
                    tvNoData.visibility = View.VISIBLE
                    return
                }

                for (facultySnap in snapshot.children) {
                    val name = facultySnap.child("name").getValue(String::class.java) ?: "-"
                    val phone = facultySnap.child("phone").getValue(String::class.java) ?: "-"

                    val card = layoutInflater.inflate(
                        R.layout.item_faculty_card, facultyContainer, false
                    ) as MaterialCardView

                    val tvName = card.findViewById<TextView>(R.id.tvFacultyName)
                    val tvPhone = card.findViewById<TextView>(R.id.tvFacultyPhone)

                    tvName.text = name
                    tvPhone.text = phone

                    // ✅ Tap-to-call feature
                    tvPhone.setOnClickListener {
                        try {
                            val dialIntent = Intent(Intent.ACTION_DIAL)
                            dialIntent.data = Uri.parse("tel:$phone")
                            startActivity(dialIntent)
                        } catch (e: Exception) {
                            Toast.makeText(
                                this@StudentFacultyContactsActivity,
                                "Unable to open dialer: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    facultyContainer.addView(card)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                progressBar.visibility = View.GONE
                Toast.makeText(
                    this@StudentFacultyContactsActivity,
                    "Error: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
