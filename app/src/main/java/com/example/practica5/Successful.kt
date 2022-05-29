package com.example.practica5

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Successful : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_successful)

        val back: Button = findViewById<Button>(R.id.backSuccessful)
        back.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}