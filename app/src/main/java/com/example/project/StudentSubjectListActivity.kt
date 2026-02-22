package com.example.project

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class StudentSubjectListActivity : AppCompatActivity() {

    private lateinit var subjectListView: LinearLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var tvNoData: TextView

    private val dbRef = FirebaseDatabase.getInstance().getReference("college_data/question_papers")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_subject_list)

        subjectListView = findViewById(R.id.subjectListContainer)
        progressBar = findViewById(R.id.progressBar)
        tvNoData = findViewById(R.id.tvNoData)

        loadSubjects()
    }

    private fun loadSubjects() {
        progressBar.visibility = View.VISIBLE
        tvNoData.visibility = View.GONE
        subjectListView.removeAllViews()

        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                progressBar.visibility = View.GONE

                if (!snapshot.exists()) {
                    tvNoData.visibility = View.VISIBLE
                    return
                }

                for (subjectSnap in snapshot.children) {
                    val subjectName = subjectSnap.key ?: continue

                    // Create a clickable card for each subject
                    val button = Button(this@StudentSubjectListActivity)
                    button.text = subjectName
                    button.setBackgroundResource(R.drawable.subject_button_bg)
                    button.setTextColor(resources.getColor(android.R.color.black))
                    button.textSize = 18f
                    button.setPadding(20, 30, 20, 30)
                    button.setOnClickListener {
                        val intent = Intent(this@StudentSubjectListActivity, StudentQuestionViewActivity::class.java)
                        intent.putExtra("subject", subjectName)
                        startActivity(intent)
                    }

                    subjectListView.addView(button)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@StudentSubjectListActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
