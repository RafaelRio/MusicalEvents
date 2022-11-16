package com.example.musicalevents.signup

import com.example.musicalevents.data.model.Userkt
import com.example.musicalevents.signup.SignUpContractKt.OnSignUpInteractorListener

class SignUpPresenterKt(var view: SignUpContractKt.View?) : SignUpContractKt.Presenter, OnSignUpInteractorListener {

    private var interactor: SignUpInteractorKt? = null

    init {
        interactor = SignUpInteractorKt(this)
    }

    override fun validateSignUp(
        user: String?,
        email: String?,
        password: String?,
        comfirmPassword: String?
    ) {
        interactor!!.validateSignUp(user, email, password!!, comfirmPassword!!)
    }

    override fun onUserEmptyError() {
        view!!.setUserEmptyError()
    }

    override fun onEmailEmptyError() {
        view!!.setEmailEmptyError()
    }

    override fun onPasswordEmptyError() {
        view!!.setPasswordEmptyError()
    }

    override fun onConfirmPasswordEmptyError() {
        view!!.setConfirmPasswordEmptyError()
    }

    override fun onPasswordError() {
        view!!.setPasswordError()
    }

    override fun onEmailError() {
        view!!.setEmailError()
    }

    override fun onPasswordDontMatch() {
        view!!.setPasswordDontMatch()
    }


    override fun onSuccess(u: Userkt) {
        view!!.onSuccess(u)
    }

    override fun onFailure(message: Int) {
        view!!.onFailure(message)
    }


    override fun onDestroy() {
        view = null
        interactor = null
    }
}