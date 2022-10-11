package com.example.musicalevents.signup;

import com.example.musicalevents.data.model.Userkt;

public class SignUpPresenter implements SignUpContract.Presenter,SignUpContract.OnSignUpInteractorListener {

    private SignUpContract.View view;
    private SignUpInteractor interactor;


    public SignUpPresenter(SignUpContract.View view) {
        this.view = view;
        interactor = new SignUpInteractor(this);
    }

    @Override
    public void validateSignUp(String user, String email, String password, String comfirmPassword) {
        interactor.validateSignUp(user, email, password, comfirmPassword);
    }

    @Override
    public void onUserEmptyError() {
        view.setUserEmptyError();
    }

    @Override
    public void onEmailEmptyError() {
        view.setEmailEmptyError();
    }

    @Override
    public void onPasswordEmptyError() {
        view.setPasswordEmptyError();
    }

    @Override
    public void onConfirmPasswordEmptyError() {
        view.setConfirmPasswordEmptyError();
    }

    @Override
    public void onPasswordError() {
        view.setPasswordError();
    }

    @Override
    public void onEmailError() {
        view.setEmailError();
    }

    @Override
    public void onPasswordDontMatch() {
        view.setPasswordDontMatch();
    }


    public void onSuccess(Userkt e) {
        view.onSuccess(e);
    }

    @Override
    public void onFailure(String message) {
        view.onFailure(message);
    }


    @Override
    public void onDestroy() {
        this.view =null;
        this.interactor = null;
    }
}
