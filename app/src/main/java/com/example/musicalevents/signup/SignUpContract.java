package com.example.musicalevents.signup;


import com.example.musicalevents.base.BasePresenter;
import com.example.musicalevents.base.IProgressView;
import com.example.musicalevents.base.OnRepositoryCallback;
import com.example.musicalevents.login.LoginContract;

public interface SignUpContract {

    interface View extends LoginContract.View , IProgressView {
        void setUserEmptyError();
        void setConfirmPasswordEmptyError();
        void setPasswordDontMatch();
        void setEmailError();
    }

    interface Presenter extends BasePresenter {
        void validateSignUp(String user,String email,String password,String comfirmPassword);

    }

    interface Repository{
        void SignUp(String user,String email,String password,String comfirmPassword);
    }

    interface OnSignUpInteractorListener extends LoginContract.OnInteractorListener , OnRepositoryCallback {
        void onUserEmptyError();
        void onConfirmPasswordEmptyError();
        void onEmailError();
        void onPasswordDontMatch();
    }




}
