package com.example.project

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.project.databinding.ActivityModuleWisePapersBinding

class ModuleWisePapersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityModuleWisePapersBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModuleWisePapersBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
