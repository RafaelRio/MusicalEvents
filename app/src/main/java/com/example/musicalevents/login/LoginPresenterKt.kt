package com.example.musicalevents.login

import com.example.musicalevents.data.model.Userkt

class LoginPresenterKt(var view : LoginContractKt.View?) : LoginContractKt.Presenter, LoginContractKt.OnInteractorListener {

    private var interactor: LoginInteractorKt? = null

    init {
        interactor = LoginInteractorKt(this)
    }

    //region metodos del contrato presenter-interactor
    override fun validateCredentials(user: Userkt?) {
        interactor!!.validateCredentials(user!!)
    }

    override fun onEmailEmptyError() {
        view!!.setEmailEmptyError()
    }

    override fun onPasswordEmptyError() {
        view!!.setPasswordEmptyError()
    }

    override fun onPasswordError() {
        view!!.setPasswordError()
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