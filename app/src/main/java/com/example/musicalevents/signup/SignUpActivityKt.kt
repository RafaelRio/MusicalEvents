package com.example.musicalevents.signup

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.musicalevents.R
import com.example.musicalevents.base.EventKt
import com.example.musicalevents.data.model.Userkt
import com.example.musicalevents.databinding.ActivitySignUpBinding
import com.example.musicalevents.login.LoginActivitykt
import com.example.musicalevents.mvp.signup.SignUpContractKt
import com.example.musicalevents.mvp.signup.SignUpPresenterKt
import com.example.musicalevents.utils.UtilsKt
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class SignUpActivityKt : AppCompatActivity(), SignUpContractKt.View {

    private lateinit var binding: ActivitySignUpBinding
    private var presenter: SignUpContractKt.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitle(R.string.tvTitleSignUp)
        UtilsKt.disableDarkMode(this)

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding.btAlreadyAccount.setOnClickListener {
            navigateUpTo(
                Intent(
                    this@SignUpActivityKt,
                    LoginActivitykt::class.java
                )
            )
        }
        /**
         * Se utiliza el metodo on backpressed para elemiminar la activivity signUpActivity y restaurar
         * la actividad anterior LoginActivity
         */
        binding.btRegistrar.setOnClickListener { view ->
            presenter!!.validateSignUp(
                binding.tieUser.text.toString(),
                binding.tieEmail.text.toString(),
                binding.tiePassword.text.toString(),
                binding.tieConfirmPassword.text.toString()
            )
        }
        presenter = SignUpPresenterKt(this)
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        //Se evitaria un futuro memory leaks
        presenter!!.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun setUserEmptyError() {
        binding.tilUser.error = getString(R.string.errUserEmpty)
    }

    override fun setEmailEmptyError() {
        binding.tilEmail.error = getString(R.string.error_EmailEmpty)
    }

    override fun setPasswordEmptyError() {
        binding.tilPassword.error = getString(R.string.errorPasswordEmpty)
    }

    override fun setConfirmPasswordEmptyError() {
        binding.tilConfirmPassword.error = getString(R.string.errorPasswordEmpty)
    }

    override fun setPasswordError() {
        binding.tilPassword.error = getString(R.string.error_passwordFormat)
    }

    override fun setEmailError() {
        binding.tilEmail.error = getString(R.string.error_email)
    }

    override fun setPasswordDontMatch() {
        Toast.makeText(this, R.string.error_password_dont_match, Toast.LENGTH_SHORT).show()
    }


    override fun onSuccess(e: Userkt) {
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