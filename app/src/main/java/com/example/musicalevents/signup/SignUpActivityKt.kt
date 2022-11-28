package com.example.musicalevents.signup

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.example.musicalevents.R
import com.example.musicalevents.base.EventKt
import com.example.musicalevents.data.model.Userkt
import com.example.musicalevents.databinding.ActivitySignUpBinding
import com.example.musicalevents.login.LoginActivitykt
import com.example.musicalevents.mvp.signup.SignUpContractKt
import com.example.musicalevents.mvp.signup.SignUpPresenterKt
import com.example.musicalevents.utils.UtilsKt
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
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
        binding.btAlreadyAccount.setOnClickListener {
            navigateUpTo(
                Intent(
                    this@SignUpActivityKt,
                    LoginActivitykt::class.java
                )
            )
        }
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
        setError(binding.tilUser, binding.tieUser, R.string.errUserEmpty)
    }

    override fun setUserNoError() {
        setNoError(binding.tilUser, binding.tieUser)
    }

    override fun setEmailEmptyError() {
        setError(binding.tilEmail, binding.tieEmail, R.string.error_EmailEmpty)
    }

    override fun setPasswordEmptyError() {
        setError(binding.tilPassword, binding.tiePassword, R.string.errorPasswordEmpty)
    }

    override fun setConfirmPasswordEmptyError() {
        setError(binding.tilConfirmPassword, binding.tieConfirmPassword, R.string.errorPasswordEmpty)
    }

    override fun setPasswordError() {
        setError(binding.tilPassword, binding.tiePassword, R.string.error_passwordFormat)
    }

    override fun setEmailError() {
        setError(binding.tilEmail, binding.tieEmail, R.string.error_email)
    }

    override fun setEmailNoError() {
        setNoError(binding.tilEmail, binding.tieEmail)
    }

    override fun setPasswordDontMatch() {
        setError(binding.tilPassword, binding.tiePassword, R.string.error_password_dont_match)
    }

    override fun setPasswordNoError() {
        setNoError(binding.tilPassword, binding.tiePassword)
    }

    private fun setError(til: TextInputLayout, tie: TextInputEditText, @StringRes error: Int) {
        til.isErrorEnabled = true
        tie.background = ContextCompat.getDrawable(this, R.drawable.background_border_red)
        til.errorIconDrawable = null
        til.error = getString(error)
        tie.requestFocus()
    }

    private fun setNoError(til: TextInputLayout, tie: TextInputEditText) {
        til.isErrorEnabled = false
        tie.background = ContextCompat.getDrawable(this, R.drawable.background_til)
        til.errorIconDrawable = null
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