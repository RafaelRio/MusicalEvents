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
        view!!.showProgress()
    }

    override fun onEmailEmptyError() {
        view!!.hideProgress()
        view!!.setEmailEmptyError()
    }

    override fun onPasswordEmptyError() {
        view!!.hideProgress()
        view!!.setPasswordEmptyError()
    }

    override fun onPasswordError() {
        view!!.hideProgress()
        view!!.setPasswordError()
    }


    override fun onSuccess(u: Userkt?) {
        view!!.hideProgress()
        view!!.onSuccess(u)
    }

    override fun onFailure(message: Int) {
        view!!.hideProgress()
        view!!.onFailure(message)
    }

    override fun onDestroy() {
        view = null
        interactor = null
    }
}