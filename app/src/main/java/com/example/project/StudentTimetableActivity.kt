package com.example.project

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project.databinding.ActivityStudentTimetableBinding
import com.google.firebase.database.*

class StudentTimetableActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStudentTimetableBinding
    private lateinit var adapter: TimetableAdapter
    private val timetableList = mutableListOf<String>()
    private val database = FirebaseDatabase.getInstance().getReference("timetable")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentTimetableBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ✅ RecyclerView setup
        adapter = TimetableAdapter(timetableList)
        binding.recyclerViewTimetable.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewTimetable.adapter = adapter

        // ✅ Spinner setup
        val sections = arrayOf("Section A", "Section B")
        val days = arrayOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")

        binding.spinnerSection.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sections)
        binding.spinnerDay.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, days)

        // ✅ Button click → load timetable
        binding.btnLoad.setOnClickListener {
            val section = if (binding.spinnerSection.selectedItem == "Section A") "sectionA" else "sectionB"
            val day = binding.spinnerDay.selectedItem.toString().lowercase()
            fetchTimetable(section, day)
        }
    }

    private fun fetchTimetable(section: String, day: String) {
        binding.progressBar.visibility = View.VISIBLE

        database.child(section).child(day)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    timetableList.clear()

                    if (snapshot.exists()) {
                        val dataMap = mutableMapOf<String, String>()
                        for (slot in snapshot.children) {
                            val timeSlot = slot.key ?: continue
                            val subjectInfo = slot.value?.toString() ?: "No Data"
                            dataMap[timeSlot] = subjectInfo
                        }

                        // ✅ Define correct order with readable time format
                        val orderedSlots = listOf(
                            "9_30_10_30" to "9:30–10:30",
                            "10_30_11_30" to "10:30–11:30",
                            "11_30_12_30" to "11:30–12:30",
                            "12_30_1_30" to "12:30–1:30 (Lunch)",
                            "1_30_2_30" to "1:30–2:30",
                            "2_30_3_30" to "2:30–3:30"
                        )

                        for ((slotKey, readableTime) in orderedSlots) {
                            val entry = dataMap[slotKey] ?: "Free"
                            timetableList.add("$readableTime → $entry")
                        }
                    } else {
                        timetableList.add("No timetable found for ${day.replaceFirstChar { it.uppercase() }}.")
                    }

                    adapter.notifyDataSetChanged()
                    binding.progressBar.visibility = View.GONE
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@StudentTimetableActivity,
                        "❌ Error loading data: ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.progressBar.visibility = View.GONE
                }
            })
    }
}
