package com.example.project

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class StudentQuestionViewActivity : AppCompatActivity() {

    private lateinit var subjectName: String
    private lateinit var twoMarksContainer: LinearLayout
    private lateinit var fiveMarksContainer: LinearLayout
    private lateinit var twelveMarksContainer: LinearLayout

    private val dbRef = FirebaseDatabase.getInstance().getReference("college_data/question_papers")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_question_view)

        subjectName = intent.getStringExtra("subject") ?: ""
        twoMarksContainer = findViewById(R.id.twoMarksContainer)
        fiveMarksContainer = findViewById(R.id.fiveMarksContainer)
        twelveMarksContainer = findViewById(R.id.twelveMarksContainer)

        if (subjectName.isEmpty()) {
            Toast.makeText(this, "No subject selected", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            loadQuestions()
        }
    }

    private fun loadQuestions() {
        dbRef.child(subjectName).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val twoMarks = snapshot.child("twoMarks").children.map { it.value.toString() }
                val fiveMarks = snapshot.child("fiveMarks").children.map { it.value.toString() }
                val twelveMarks = snapshot.child("twelveMarks").children.map { it.value.toString() }

                populateContainer(twoMarksContainer, twoMarks)
                populateContainer(fiveMarksContainer, fiveMarks)
                populateContainer(twelveMarksContainer, twelveMarks)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@StudentQuestionViewActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun populateContainer(container: LinearLayout, questions: List<String>) {
        container.removeAllViews()
        for (q in questions) {
            val tv = TextView(this)
            tv.text = "• $q"
            tv.textSize = 16f
            tv.setPadding(10, 10, 10, 10)
            container.addView(tv)
        }
    }
}
