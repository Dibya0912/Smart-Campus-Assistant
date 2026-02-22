package com.example.project

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class AdminModulePaperActivity : AppCompatActivity() {

    private lateinit var subjectEditText: EditText
    private lateinit var twoMarksContainer: LinearLayout
    private lateinit var fiveMarksContainer: LinearLayout
    private lateinit var twelveMarksContainer: LinearLayout
    private lateinit var uploadButton: Button

    private val databaseRef = FirebaseDatabase.getInstance().getReference("college_data/question_papers")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_module_paper)

        subjectEditText = findViewById(R.id.etSubjectName)
        twoMarksContainer = findViewById(R.id.twoMarksContainer)
        fiveMarksContainer = findViewById(R.id.fiveMarksContainer)
        twelveMarksContainer = findViewById(R.id.twelveMarksContainer)
        uploadButton = findViewById(R.id.btnUpload)

        // --- Dynamically create question fields ---
        createQuestionFields(twoMarksContainer, 10, "2 Marks Question ")
        createQuestionFields(fiveMarksContainer, 6, "5 Marks Question ")
        createQuestionFields(twelveMarksContainer, 6, "12 Marks Question ")

        uploadButton.setOnClickListener {
            uploadQuestions()
        }
    }

    private fun createQuestionFields(container: LinearLayout, count: Int, label: String) {
        for (i in 1..count) {
            val editText = EditText(this)
            editText.hint = "$label $i"
            editText.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 8, 0, 8)
            }
            container.addView(editText)
        }
    }

    private fun uploadQuestions() {
        val subject = subjectEditText.text.toString().trim()
        if (subject.isEmpty()) {
            Toast.makeText(this, "Please enter subject name", Toast.LENGTH_SHORT).show()
            return
        }

        val twoMarks = getQuestionsFromContainer(twoMarksContainer)
        val fiveMarks = getQuestionsFromContainer(fiveMarksContainer)
        val twelveMarks = getQuestionsFromContainer(twelveMarksContainer)

        val subjectData = mapOf(
            "twoMarks" to twoMarks,
            "fiveMarks" to fiveMarks,
            "twelveMarks" to twelveMarks
        )

        databaseRef.child(subject).setValue(subjectData)
            .addOnSuccessListener {
                Toast.makeText(this, "✅ Questions uploaded successfully!", Toast.LENGTH_SHORT).show()
                subjectEditText.text.clear()
                twoMarksContainer.removeAllViews()
                fiveMarksContainer.removeAllViews()
                twelveMarksContainer.removeAllViews()
                createQuestionFields(twoMarksContainer, 10, "2 Marks Question ")
                createQuestionFields(fiveMarksContainer, 6, "5 Marks Question ")
                createQuestionFields(twelveMarksContainer, 6, "12 Marks Question ")
            }
            .addOnFailureListener {
                Toast.makeText(this, "❌ Upload failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun getQuestionsFromContainer(container: LinearLayout): List<String> {
        val questions = mutableListOf<String>()
        for (i in 0 until container.childCount) {
            val view = container.getChildAt(i)
            if (view is EditText) {
                val text = view.text.toString().trim()
                if (text.isNotEmpty()) questions.add(text)
            }
        }
        return questions
    }
}
