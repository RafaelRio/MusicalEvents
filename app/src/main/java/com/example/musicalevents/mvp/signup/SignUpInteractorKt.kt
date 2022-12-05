package com.example.musicalevents.mvp.signup

import android.os.Handler
import android.text.TextUtils
import android.util.Patterns
import com.example.musicalevents.base.OnRepositoryCallback
import com.example.musicalevents.data.model.Userkt
import com.example.musicalevents.data.repository.LoginRepository
import com.example.musicalevents.utils.UtilsKt.Companion.isPasswordValid

class SignUpInteractorKt(var listener: SignUpContractKt.OnSignUpInteractorListener) :
    OnRepositoryCallback {

    fun validateSignUp(user: String?, email: String?, password: String, comfirmPassword: String) {
        Handler().postDelayed(Runnable { // A gestionar las alternativas del caso de uso
            if (TextUtils.isEmpty(user)) {
                listener.onUserEmptyError()
                return@Runnable
            }
            if (TextUtils.isEmpty(email)) {
                listener.onEmailEmptyError()
                return@Runnable
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                listener.onEmailError()
                return@Runnable
            }
            if (TextUtils.isEmpty(password)) {
                listener.onPasswordEmptyError()
                return@Runnable
            }
            if (TextUtils.isEmpty(comfirmPassword)) {
                listener.onConfirmPasswordEmptyError()
                return@Runnable
            }
            if (!isPasswordValid(password)) {
                listener.onPasswordError()
                return@Runnable
            }
            if (password != comfirmPassword) {
                listener.onPasswordDontMatch()
                return@Runnable
            }
            LoginRepository.getInstance(this@SignUpInteractorKt)
                .SignUp(user, email, password, comfirmPassword)
        }, 2000)
    }

    //Estos metodos vienen de la respuesta que nos da el Repositorio
    override fun onSuccess(u: Userkt) {
        listener.onSuccess(u)
    }

    override fun onFailure(message: Int) {
        listener.onFailure(message)
    }
}
