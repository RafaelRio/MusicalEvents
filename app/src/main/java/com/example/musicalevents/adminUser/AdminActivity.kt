package com.example.musicalevents.adminUser

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.musicalevents.R
import com.example.musicalevents.utils.UtilsKt

class AdminActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        UtilsKt.disableDarkMode(this)
    }
}