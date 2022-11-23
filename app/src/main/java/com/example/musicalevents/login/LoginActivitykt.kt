package com.example.musicalevents.login

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.musicalevents.R
import com.example.musicalevents.adminUser.AdminActivity
import com.example.musicalevents.base.EventKt
import com.example.musicalevents.data.model.Userkt
import com.example.musicalevents.data.repository.LoginRepository
import com.example.musicalevents.databinding.ActivityLoginBinding
import com.example.musicalevents.mvp.login.LoginContractKt
import com.example.musicalevents.mvp.login.LoginPresenterKt
import com.example.musicalevents.normalUser.MainActivity
import com.example.musicalevents.signup.SignUpActivityKt
import com.example.musicalevents.utils.UtilsKt
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.lang.Boolean
import kotlin.Int
import kotlin.toString


class LoginActivitykt : AppCompatActivity(), LoginContractKt.View {

    var e1 = Userkt()
    private lateinit var binding: ActivityLoginBinding
    private var presenter: LoginContractKt.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(R.string.tvLogin_login)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val decorView = window.decorView
        val uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN
        decorView.systemUiVisibility = uiOptions
        UtilsKt.disableDarkMode(this)
        binding.btSignUp.setOnClickListener { view -> startActivityLogin() }
        binding.btSignIn.setOnClickListener { view ->
            e1.email = binding.tieEmail.text.toString()
            e1.password = binding.tiePassword.text.toString()
            presenter!!.validateCredentials(e1)
        }
        presenter = LoginPresenterKt(this)
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        //Se evitaria un futuro memory leaks
        presenter!!.onDestroy()
        //Se quita como subcriptor del EventBus
        EventBus.getDefault().unregister(this)
    }

    private fun startActivityLogin() {
        startActivity(Intent(this, SignUpActivityKt::class.java))
    }

    override fun setEmailEmptyError() {
        binding.tilEmail.error = getString(R.string.error_EmailEmpty)
    }

    override fun setPasswordEmptyError() {
        binding.tilPassword.error = getString(R.string.errorPasswordEmpty)
    }

    override fun setPasswordError() {
        binding.tilPassword.error = getString(R.string.error_passwordFormat)
    }

    override fun onSuccess(u: Userkt) {
        if (binding.ckremember.isChecked) {
            val editor = PreferenceManager.getDefaultSharedPreferences(this).edit()
            editor.putBoolean("admin", u.isAdmin)
            editor.putString("email", u.email)
            editor.putString("password", u.password)
            editor.putString("instagram", u.instagram)
            editor.putString("twitter", u.twitter)
            editor.putString("facebook", u.facebook)
            editor.putString("website", u.website)
            LoginRepository.currentUser = u
            editor.apply()
        }

        if (Boolean.TRUE == u.isAdmin) {
            //Carga una vista
            startAdminActivity()
        } else if (Boolean.FALSE == u.isAdmin) {
            //Carga otra vista
            startMainActivity()
        }
    }

    private fun startAdminActivity() {
        startActivity(Intent(this@LoginActivitykt, AdminActivity::class.java))
        finish()
    }

    private fun startMainActivity() {
        startActivity(Intent(this@LoginActivitykt, MainActivity::class.java))
        finish()
    }

    override fun onFailure(message: Int) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    @Subscribe
    fun onEvent(event: EventKt) {
        Toast.makeText(this, event.message, Toast.LENGTH_SHORT).show()
    }
}