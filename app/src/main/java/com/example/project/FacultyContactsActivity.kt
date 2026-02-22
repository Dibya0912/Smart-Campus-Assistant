package com.example.project

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class FacultyContactsActivity : AppCompatActivity() {

    private val dbRef = FirebaseDatabase.getInstance().getReference("college_data/cr_contacts")

    private lateinit var progressBar: ProgressBar
    private lateinit var tvSectionABoy: TextView
    private lateinit var tvSectionABoyPhone: TextView
    private lateinit var tvSectionAGirl: TextView
    private lateinit var tvSectionAGirlPhone: TextView
    private lateinit var tvSectionBBoy: TextView
    private lateinit var tvSectionBBoyPhone: TextView
    private lateinit var tvSectionBGirl: TextView
    private lateinit var tvSectionBGirlPhone: TextView
    private lateinit var tvNoData: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faculty_contacts)

        progressBar = findViewById(R.id.progressBar)
        tvSectionABoy = findViewById(R.id.tvSectionABoy)
        tvSectionABoyPhone = findViewById(R.id.tvSectionABoyPhone)
        tvSectionAGirl = findViewById(R.id.tvSectionAGirl)
        tvSectionAGirlPhone = findViewById(R.id.tvSectionAGirlPhone)
        tvSectionBBoy = findViewById(R.id.tvSectionBBoy)
        tvSectionBBoyPhone = findViewById(R.id.tvSectionBBoyPhone)
        tvSectionBGirl = findViewById(R.id.tvSectionBGirl)
        tvSectionBGirlPhone = findViewById(R.id.tvSectionBGirlPhone)
        tvNoData = findViewById(R.id.tvNoData)

        loadCrContacts()
    }

    private fun loadCrContacts() {
        progressBar.visibility = View.VISIBLE
        tvNoData.visibility = View.GONE

        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                progressBar.visibility = View.GONE

                if (!snapshot.exists()) {
                    tvNoData.visibility = View.VISIBLE
                    return
                }

                // Fetch Section A
                val aBoyName = snapshot.child("sectionA/boy/name").getValue(String::class.java) ?: "-"
                val aBoyPhone = snapshot.child("sectionA/boy/phone").getValue(String::class.java) ?: "-"
                val aGirlName = snapshot.child("sectionA/girl/name").getValue(String::class.java) ?: "-"
                val aGirlPhone = snapshot.child("sectionA/girl/phone").getValue(String::class.java) ?: "-"

                // Fetch Section B
                val bBoyName = snapshot.child("sectionB/boy/name").getValue(String::class.java) ?: "-"
                val bBoyPhone = snapshot.child("sectionB/boy/phone").getValue(String::class.java) ?: "-"
                val bGirlName = snapshot.child("sectionB/girl/name").getValue(String::class.java) ?: "-"
                val bGirlPhone = snapshot.child("sectionB/girl/phone").getValue(String::class.java) ?: "-"

                // Update TextViews
                tvSectionABoy.text = aBoyName
                tvSectionABoyPhone.text = aBoyPhone
                tvSectionAGirl.text = aGirlName
                tvSectionAGirlPhone.text = aGirlPhone
                tvSectionBBoy.text = bBoyName
                tvSectionBBoyPhone.text = bBoyPhone
                tvSectionBGirl.text = bGirlName
                tvSectionBGirlPhone.text = bGirlPhone
            }

            override fun onCancelled(error: DatabaseError) {
                progressBar.visibility = View.GONE
                tvNoData.visibility = View.VISIBLE
                Toast.makeText(this@FacultyContactsActivity, "Error loading data: ${error.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
