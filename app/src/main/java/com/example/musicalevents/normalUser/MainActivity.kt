package com.example.musicalevents.normalUser

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.musicalevents.R
import com.example.musicalevents.utils.UtilsKt

/**
 * Activity que carga todos los fragments de un usuario sin permisos de escritura sobre la base de datos
 * @author Rafa
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        UtilsKt.disableDarkMode(this)
    }
}