package com.example.musicalevents.signup

import com.example.musicalevents.base.BasePresenter
import com.example.musicalevents.base.IProgressView
import com.example.musicalevents.base.OnRepositoryCallback
import com.example.musicalevents.login.LoginContractKt
import com.example.musicalevents.login.LoginContractKt.OnInteractorListener

interface SignUpContractKt {

    interface View : LoginContractKt.View, IProgressView {
        fun setUserEmptyError()
        fun setConfirmPasswordEmptyError()
        fun setPasswordDontMatch()
        fun setEmailError()
    }

    interface Presenter : BasePresenter {
        fun validateSignUp(
            user: String?,
            email: String?,
            password: String?,
            comfirmPassword: String?
        )
    }

    interface Repository {
        fun SignUp(user: String?, email: String?, password: String?, comfirmPassword: String?)
    }

    interface OnSignUpInteractorListener : OnInteractorListener, OnRepositoryCallback {
        fun onUserEmptyError()
        fun onConfirmPasswordEmptyError()
        fun onEmailError()
        fun onPasswordDontMatch()
    }
}