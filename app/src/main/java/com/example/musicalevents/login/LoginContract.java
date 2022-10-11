package com.example.musicalevents.login;

import com.example.musicalevents.base.BasePresenter;
import com.example.musicalevents.base.IProgressView;
import com.example.musicalevents.base.OnRepositoryCallback;
import com.example.musicalevents.data.model.Userkt;

public interface LoginContract {
    interface View extends OnRepositoryCallback, IProgressView {
        //Alternativas del caso de uso ,,set porque se modifica elementos de la vista
        void setEmailEmptyError();
        void setPasswordEmptyError();
        void setPasswordError();
    }

    /**
     * Interfaz que debe implementar el presenter
     */
    interface Presenter extends BasePresenter {
        void validateCredentials(Userkt user);
    }

    /**
     * Toda clase que quiera ser un repositorio para login
     */
    interface Repository {
        void login(Userkt user);
    }

    /**
     * Interfaz que debe implementar el listener de interactor
     * esta interfaz es las posibles alternativas del caso de uso de LOGIN
     */
    interface OnInteractorListener extends OnRepositoryCallback {
        //Alternativa del caso de uso
        void onEmailEmptyError();
        void onPasswordEmptyError();
        void onPasswordError();
    }
}
