package com.example.project

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class UpdateCrContactActivity : AppCompatActivity() {

    // Use same path as CR contact viewer
    private val dbRef = FirebaseDatabase.getInstance().getReference("college_data/cr_contacts")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_cr_contact)

        val nameA_boy = findViewById<EditText>(R.id.etSectionABoyName)
        val phoneA_boy = findViewById<EditText>(R.id.etSectionABoyPhone)
        val nameA_girl = findViewById<EditText>(R.id.etSectionAGirlName)
        val phoneA_girl = findViewById<EditText>(R.id.etSectionAGirlPhone)
        val nameB_boy = findViewById<EditText>(R.id.etSectionBBoyName)
        val phoneB_boy = findViewById<EditText>(R.id.etSectionBBoyPhone)
        val nameB_girl = findViewById<EditText>(R.id.etSectionBGirlName)
        val phoneB_girl = findViewById<EditText>(R.id.etSectionBGirlPhone)
        val btnSave = findViewById<Button>(R.id.btnSaveCrContacts)

        btnSave.setOnClickListener {
            val crData = mapOf(
                "sectionA" to mapOf(
                    "boy" to mapOf("name" to nameA_boy.text.toString(), "phone" to phoneA_boy.text.toString()),
                    "girl" to mapOf("name" to nameA_girl.text.toString(), "phone" to phoneA_girl.text.toString())
                ),
                "sectionB" to mapOf(
                    "boy" to mapOf("name" to nameB_boy.text.toString(), "phone" to phoneB_boy.text.toString()),
                    "girl" to mapOf("name" to nameB_girl.text.toString(), "phone" to phoneB_girl.text.toString())
                )
            )

            dbRef.setValue(crData)
                .addOnSuccessListener {
                    Toast.makeText(this, "✅ CR contacts updated successfully!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "❌ Failed to update: ${it.message}", Toast.LENGTH_LONG).show()
                }
        }
    }
}
