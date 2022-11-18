package com.example.musicalevents.normalUser

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.musicalevents.R
import com.example.musicalevents.utils.UtilsKt

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        UtilsKt.disableDarkMode(this)
    }
}