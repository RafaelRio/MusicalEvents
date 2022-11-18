package com.example.musicalevents.login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.musicalevents.R
import com.example.musicalevents.adminUser.AdminActivity
import com.example.musicalevents.base.EventKt
import com.example.musicalevents.data.model.Userkt
import com.example.musicalevents.databinding.ActivityLoginBinding
import com.example.musicalevents.normalUser.MainActivity
import com.example.musicalevents.signup.SignUpActivityKt
import com.example.musicalevents.utils.UtilsKt
import com.example.musicalevents.utils.UtilsKt.Companion.isPasswordValid
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.lang.Boolean
import kotlin.Int
import kotlin.String
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
        // Hide the status bar.
        val uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN
        decorView.systemUiVisibility = uiOptions
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.

        UtilsKt.disableDarkMode(this)
        binding.btSignUp.setOnClickListener { view -> startActivityLogin() }
        binding.btSignIn.setOnClickListener { view ->
            e1.email = binding.tieEmail.text.toString()
            e1.password = binding.tiePassword.text.toString()
            presenter!!.validateCredentials(e1)
        }
        presenter = LoginPresenterKt(this)
        //la vista se registra como subscriptor del EventBus
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


    //region Metodos del contrato LoginContract.View
    override fun setEmailEmptyError() {
        binding.tilEmail.error = getString(R.string.error_EmailEmpty)
    }

    override fun setPasswordEmptyError() {
        binding.tilPassword.error = getString(R.string.errorPasswordEmpty)
    }

    override fun setPasswordError() {
        binding.tilPassword.error = getString(R.string.error_passwordFormat)
    }


    //endregion
    //region Metodos del contrato con LoginContract.View extend OnLoginListener es decir son los metodos que obtiene por herencia de la otra interfaz
    override fun onSuccess(u: Userkt) {
//        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
////        editor.putBoolean("isAdmin", e.isAdmin());
////        editor.apply();
//        if (binding.chkRemember.isChecked()) {
//            editor.putString(Userkt.TAG, binding.tieEmail.getText().toString());
//            editor.apply();
//            //O BIEN APPLY O BIEN COMMIT QUE SINO NO SE HACEN LOS CAMBIOS EN EK FICHERO
//
//        }


//




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

    //endregion
    //region Clase interna que controla cada vez que el usuario introduce un caracter en un editable
    // TExtInputLayout si cumple o no las reglas de negocio
    private fun validatePassword(password: String) {
        if (TextUtils.isEmpty(password)) {
            binding.tilPassword.error = getString(R.string.errorPasswordEmpty)
        } else if (!isPasswordValid(password)) {
            binding.tilPassword.error = getString(R.string.error_passwordFormat)
        } else {
            //desaparece el error
            binding.tilPassword.error = null
        }
    }

    private fun validateEmail(email: String) {
        if (TextUtils.isEmpty(email)) {
            binding.tilEmail.error = getString(R.string.error_EmailEmpty)
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.error = getString(R.string.error_email)
        } else {
            //desaparece el error
            binding.tilEmail.error = null
        }
    }

    @Subscribe
    fun onEvent(event: EventKt) {
        Toast.makeText(this, event.message, Toast.LENGTH_SHORT).show()
    }
}