package com.example.musicalevents.utils

import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import com.example.musicalevents.adminUser.AdminActivity
import com.example.musicalevents.login.LoginActivitykt
import com.example.musicalevents.normalUser.MainActivity

class SplashActivity : AppCompatActivity() {

    override fun onStart() {
        super.onStart()
        Handler(Looper.getMainLooper()).postDelayed({
            if (saveSession()) {
                startApp()
            } else {
                starLogin()
            }
        }, 200)
    }

    private fun starLogin() {
        startActivity(Intent(this@SplashActivity, LoginActivitykt::class.java))
        finish()
    }

    private fun saveSession(): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(this).contains("email")
    }

    private fun startApp() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val admin = prefs.getBoolean("admin", false)
        if (admin) {
            startActivity(Intent(this, AdminActivity::class.java))
        } else {
            startActivity(Intent(this, MainActivity::class.java))
        }
        finish()
    }

}