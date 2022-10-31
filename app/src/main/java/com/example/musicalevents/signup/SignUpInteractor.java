package com.example.musicalevents.signup;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Patterns;

import com.example.musicalevents.base.OnRepositoryCallback;
import com.example.musicalevents.data.model.Userkt;
import com.example.musicalevents.data.repository.LoginRepository;
import com.example.musicalevents.utils.CommonUtils;

public class SignUpInteractor implements OnRepositoryCallback {

    private SignUpContract.OnSignUpInteractorListener listener;

    public SignUpInteractor(SignUpContract.OnSignUpInteractorListener listener) {
        this.listener = listener;

    }

    public void validateSignUp(String user, String email, String password, String comfirmPassword) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // A gestionar las alternativas del caso de uso
                if (TextUtils.isEmpty(user)){
                    listener.onUserEmptyError();
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    listener.onPasswordEmptyError();
                    return;
                }
                if (TextUtils.isEmpty(comfirmPassword)){
                    listener.onConfirmPasswordEmptyError();
                    return;
                }
                if (!CommonUtils.isPasswordValid(password)){
                    listener.onPasswordError();
                    return;
                }
                if (TextUtils.isEmpty(email)){
                    listener.onEmailEmptyError();
                    return;
                }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    listener.onEmailError();
                    return;
                }

                if (!password.equals(comfirmPassword)){
                    listener.onPasswordDontMatch();
                    return;
                }
                LoginRepository.getInstance(SignUpInteractor.this).SignUp(user, email, password, comfirmPassword);


            }
        }, 2000);
    }

    //Estos metodos vienen de la respuesta que nos da el Repositorio
    @Override
    public void onSuccess(Userkt e) {
        listener.onSuccess(e);
    }

    @Override
    public void onFailure(int message) {
        listener.onFailure(message);
    }
}
