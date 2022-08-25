package com.example.noteroom.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.noteroom.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        eg.setOnClickListener {
            EgActivity.open(this)
        }
    }
}