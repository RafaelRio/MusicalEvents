package com.example.musicalevents.login;

import android.text.TextUtils;

import com.example.musicalevents.base.OnRepositoryCallback;
import com.example.musicalevents.data.model.Userkt;
import com.example.musicalevents.data.repository.LoginRepository;
import com.example.musicalevents.utils.CommonUtils;

public class LoginInteractor implements OnRepositoryCallback {

    private LoginContract.Repository repository;
    private LoginContract.OnInteractorListener listener;

    //El presentar que quiera utilizar esta clase debe implementar esta interfaz
    public LoginInteractor(LoginContract.OnInteractorListener listener) {
        this.listener = listener;
        //this.repository = LoginRepositoryImpl.getInstance(this);
    }

    public void validateCredentials(Userkt user) {
        //En base a lo que suceda avisara a su Listener//Presentador
        if (TextUtils.isEmpty(user.getEmail())) {
            listener.onEmailEmptyError();
            return;
        }
        if (TextUtils.isEmpty(user.getPassword())) {
            listener.onPasswordEmptyError();
            return;
        }
        if (!CommonUtils.isPasswordValid(user.getPassword())) {
            listener.onPasswordError();
            return;
        }
        LoginRepository.getInstance(this).login(user);


    }


    //Estos metodos vienen de la respuesta que nos da el Repositorio
    @Override
    public void onSuccess(Userkt e) {
        listener.onSuccess(e);
    }

    @Override
    public void onFailure(String message) {
        listener.onFailure(message);
    }
}
