package com.example.musicalevents.mvp.login

import android.text.TextUtils
import com.example.musicalevents.base.OnRepositoryCallback
import com.example.musicalevents.data.model.Userkt
import com.example.musicalevents.data.repository.LoginRepository
import com.example.musicalevents.mvp.login.LoginContractKt.OnInteractorListener
import com.example.musicalevents.utils.UtilsKt.isPasswordValid

class LoginInteractorKt(var listener: OnInteractorListener?) : OnRepositoryCallback {

    fun validateCredentials(user: Userkt) {
        //En base a lo que suceda avisara a su Listener//Presentador
        if (TextUtils.isEmpty(user.email)) {
            listener!!.onEmailEmptyError()
            return
        }
        if (TextUtils.isEmpty(user.password)) {
            listener!!.onPasswordEmptyError()
            return
        }
        if (!user.password?.let { isPasswordValid(it) }!!) {
            listener!!.onPasswordError()
            return
        }
        LoginRepository.getInstance(this).login(user)
    }


    //This methods come from the Repository callback
    override fun onSuccess(u: Userkt) {
        listener!!.onSuccess(u)
    }

    override fun onFailure(message: Int) {
        listener!!.onFailure(message)
    }
}