package com.example.musicalevents.mvp.signup

import com.example.musicalevents.base.BasePresenterKt
import com.example.musicalevents.base.OnRepositoryCallback
import com.example.musicalevents.mvp.login.LoginContractKt
import com.example.musicalevents.mvp.login.LoginContractKt.OnInteractorListener

interface SignUpContractKt {

    interface View : LoginContractKt.View {
        fun setUserEmptyError()
        fun setConfirmPasswordEmptyError()
        fun setPasswordDontMatch()
        fun setEmailError()
    }

    interface Presenter : BasePresenterKt {
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