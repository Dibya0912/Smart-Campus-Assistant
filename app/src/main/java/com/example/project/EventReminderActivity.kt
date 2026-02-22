package com.example.project

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project.databinding.ActivityEventReminderBinding
import com.google.firebase.database.*

class EventReminderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEventReminderBinding
    private lateinit var database: DatabaseReference
    private lateinit var adapter: EventAdapter
    private val eventList = mutableListOf<EventModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventReminderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().getReference("events")

        adapter = EventAdapter(
            eventList,
            onItemClick = { event -> showEventDetails(event) },
            onItemLongClick = { /* no long-click needed in student view */ }
        )


        binding.recyclerViewEvents.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewEvents.adapter = adapter

        loadEvents()
    }

    private fun loadEvents() {
        binding.progressBar.visibility = View.VISIBLE

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                eventList.clear()
                for (child in snapshot.children) {
                    val event = child.getValue(EventModel::class.java)
                    event?.let { eventList.add(it) }
                }

                if (eventList.isEmpty()) {
                    Toast.makeText(this@EventReminderActivity, "No events available", Toast.LENGTH_SHORT).show()
                }

                adapter.notifyDataSetChanged()
                binding.progressBar.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@EventReminderActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
            }
        })
    }

    private fun showEventDetails(event: EventModel) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_event_details, null)
        val tvTitle = dialogView.findViewById<TextView>(R.id.tvDialogTitle)
        val tvDescription = dialogView.findViewById<TextView>(R.id.tvDialogDescription)
        val tvHost = dialogView.findViewById<TextView>(R.id.tvDialogHost)
        val tvDateTime = dialogView.findViewById<TextView>(R.id.tvDialogDateTime)

        tvTitle.text = event.title
        tvDescription.text = event.description
        tvHost.text = "Hosted by: ${event.host}"
        tvDateTime.text = "📅 ${event.date}   🕒 ${event.time}"

        AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("Close", null)
            .create()
            .show()
    }
}
