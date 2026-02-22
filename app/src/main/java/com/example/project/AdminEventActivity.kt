    package com.example.project

    import android.app.DatePickerDialog
    import android.app.TimePickerDialog
    import android.os.Bundle
    import android.widget.*
    import androidx.appcompat.app.AlertDialog
    import androidx.appcompat.app.AppCompatActivity
    import androidx.recyclerview.widget.LinearLayoutManager
    import com.google.firebase.database.*
    import java.util.*

    data class EventModel(
        var id: String? = null,
        var title: String? = null,
        var description: String? = null,
        var host: String? = null,
        var eligible: String? = null,
        var date: String? = null,
        var time: String? = null
    )

    class AdminEventActivity : AppCompatActivity() {

        private lateinit var database: DatabaseReference
        private lateinit var adapter: EventAdapter
        private val eventList = mutableListOf<EventModel>()

        private var selectedEventId: String? = null // for edit mode

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_admin_event)

            database = FirebaseDatabase.getInstance().getReference("events")

            val etTitle = findViewById<EditText>(R.id.etEventTitle)
            val etDesc = findViewById<EditText>(R.id.etEventDescription)
            val etHost = findViewById<EditText>(R.id.etEventHost)
            val etDate = findViewById<EditText>(R.id.etEventDate)
            val etTime = findViewById<EditText>(R.id.etEventTime)
            val spinnerEligible = findViewById<Spinner>(R.id.spinnerEligible)
            val btnAdd = findViewById<Button>(R.id.btnAddEvent)
            val recyclerView = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recyclerViewEvents)

            val eligibleOptions = arrayOf("All Students", "Section A", "Section B", "CSE", "ECE", "MECH")
            spinnerEligible.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, eligibleOptions)

            // Date Picker
            etDate.setOnClickListener {
                val cal = Calendar.getInstance()
                DatePickerDialog(this, { _, y, m, d ->
                    etDate.setText("$y-${m + 1}-$d")
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
            }

            // Time Picker
            etTime.setOnClickListener {
                val cal = Calendar.getInstance()
                TimePickerDialog(this, { _, h, min ->
                    val amPm = if (h >= 12) "PM" else "AM"
                    val hour = if (h > 12) h - 12 else h
                    etTime.setText(String.format("%02d:%02d %s", hour, min, amPm))
                }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false).show()
            }

            // RecyclerView setup
            adapter = EventAdapter(eventList,
                onItemClick = { event -> fillFormForEdit(event, etTitle, etDesc, etHost, etDate, etTime, spinnerEligible, btnAdd) },
                onItemLongClick = { event -> confirmDelete(event) }
            )

            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = adapter

            // Load existing events
            loadEvents()

            // Add or Update button
            btnAdd.setOnClickListener {
                val title = etTitle.text.toString()
                val desc = etDesc.text.toString()
                val host = etHost.text.toString()
                val date = etDate.text.toString()
                val time = etTime.text.toString()
                val eligible = spinnerEligible.selectedItem.toString()

                if (title.isEmpty() || desc.isEmpty() || host.isEmpty() || date.isEmpty() || time.isEmpty()) {
                    Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (selectedEventId == null) {
                    // Add new event
                    val id = database.push().key ?: UUID.randomUUID().toString()
                    val event = EventModel(id, title, desc, host, eligible, date, time)
                    database.child(id).setValue(event)
                        .addOnSuccessListener {
                            Toast.makeText(this, "✅ Event Added!", Toast.LENGTH_SHORT).show()
                            clearFields(etTitle, etDesc, etHost, etDate, etTime)
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "❌ Failed: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    // Update existing event
                    val event = EventModel(selectedEventId, title, desc, host, eligible, date, time)
                    database.child(selectedEventId!!).setValue(event)
                        .addOnSuccessListener {
                            Toast.makeText(this, "✅ Event Updated!", Toast.LENGTH_SHORT).show()
                            clearFields(etTitle, etDesc, etHost, etDate, etTime)
                            btnAdd.text = "Add / Update Event"
                            selectedEventId = null
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "❌ Update Failed: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }

        private fun loadEvents() {
            database.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    eventList.clear()
                    for (child in snapshot.children) {
                        val event = child.getValue(EventModel::class.java)
                        event?.let { eventList.add(it) }
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }

        private fun fillFormForEdit(
            event: EventModel,
            etTitle: EditText,
            etDesc: EditText,
            etHost: EditText,
            etDate: EditText,
            etTime: EditText,
            spinnerEligible: Spinner,
            btnAdd: Button
        ) {
            etTitle.setText(event.title)
            etDesc.setText(event.description)
            etHost.setText(event.host)
            etDate.setText(event.date)
            etTime.setText(event.time)

            val eligibleIndex = (spinnerEligible.adapter as ArrayAdapter<String>).getPosition(event.eligible)
            spinnerEligible.setSelection(eligibleIndex)

            btnAdd.text = "Update Event"
            selectedEventId = event.id
        }

        private fun confirmDelete(event: EventModel) {
            AlertDialog.Builder(this)
                .setTitle("Delete Event")
                .setMessage("Are you sure you want to delete '${event.title}'?")
                .setPositiveButton("Delete") { _, _ ->
                    database.child(event.id!!).removeValue()
                        .addOnSuccessListener {
                            Toast.makeText(this, "🗑️ Event Deleted", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "❌ Failed: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        private fun clearFields(vararg fields: EditText) {
            fields.forEach { it.text.clear() }
        }
    }
