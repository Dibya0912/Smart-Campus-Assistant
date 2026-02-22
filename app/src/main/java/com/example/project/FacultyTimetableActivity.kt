package com.example.project

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class FacultyTimetableActivity : AppCompatActivity() {

    private lateinit var spinnerDay: Spinner
    private lateinit var spinnerFaculty: Spinner
    private lateinit var btnShow: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: TimetableAdapter
    private val timetableList = mutableListOf<String>()
    private val database = FirebaseDatabase.getInstance().getReference("timetable")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faculty_timetable)

        spinnerDay = findViewById(R.id.spinnerDay)
        spinnerFaculty = findViewById(R.id.spinnerFaculty)
        btnShow = findViewById(R.id.btnShowFacultyTimetable)
        recyclerView = findViewById(R.id.recyclerViewFacultyTimetable)
        progressBar = findViewById(R.id.progressBar)

        adapter = TimetableAdapter(timetableList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Setup spinners
        val days = arrayOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
        val faculties = arrayOf(
            "Prof. A Sharma",
            "Prof. B Gupta",
            "Prof. C Mehta",
            "Prof. D Patel",
            "Prof. E Roy"
        )

        spinnerDay.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, days)
        spinnerFaculty.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, faculties)

        btnShow.setOnClickListener {
            val selectedDay = spinnerDay.selectedItem.toString().lowercase()
            val selectedFaculty = spinnerFaculty.selectedItem.toString()

            if (selectedDay.isEmpty() || selectedFaculty.isEmpty()) {
                Toast.makeText(this, "Please select both Day and Faculty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            loadFacultySchedule(selectedDay, selectedFaculty)
        }
    }

    private fun loadFacultySchedule(day: String, facultyName: String) {
        progressBar.visibility = View.VISIBLE
        timetableList.clear()

        val sections = listOf("sectionA", "sectionB")
        var tasksDone = 0

        for (section in sections) {
            database.child(section).child(day)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (slot in snapshot.children) {
                            val timeSlot = slot.key ?: continue
                            val entry = slot.value.toString()

                            if (entry.contains(facultyName, ignoreCase = true)) {
                                val readableTime = when (timeSlot) {
                                    "9_30_10_30" -> "9:30–10:30"
                                    "10_30_11_30" -> "10:30–11:30"
                                    "11_30_12_30" -> "11:30–12:30"
                                    "12_30_1_30" -> "12:30–1:30 (Lunch)"
                                    "1_30_2_30" -> "1:30–2:30"
                                    "2_30_3_30" -> "2:30–3:30"
                                    else -> timeSlot
                                }
                                timetableList.add("$readableTime → $entry ($section)")
                            }
                        }
                        tasksDone++
                        if (tasksDone == sections.size) {
                            adapter.notifyDataSetChanged()
                            progressBar.visibility = View.GONE
                            if (timetableList.isEmpty()) {
                                Toast.makeText(
                                    this@FacultyTimetableActivity,
                                    "No classes found for $facultyName on ${day.replaceFirstChar { it.uppercase() }}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        progressBar.visibility = View.GONE
                        Toast.makeText(
                            this@FacultyTimetableActivity,
                            "Error: ${error.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }
    }
}
