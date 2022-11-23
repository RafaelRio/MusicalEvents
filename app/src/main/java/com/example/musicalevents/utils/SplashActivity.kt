package com.example.musicalevents.utils

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import com.example.musicalevents.adminUser.AdminActivity
import com.example.musicalevents.login.LoginActivitykt
import com.example.musicalevents.normalUser.MainActivity

class SplashActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onStart() {
        super.onStart()
        Handler().postDelayed({
            if (saveSession()) {
                startApp()
            } else {
                starLogin()
            }
        }, 200)
    }

    private fun starLogin() {
        startActivity(Intent(this@SplashActivity, LoginActivitykt::class.java))
        //Voy a llamar de forma explicita al metodo finish() de una activity, para eliminar
        //esta activity de la pila de actividades, porque si el usuario pulsa back no que se visualice de nuevo.
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