package com.example.musicalevents.login;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.musicalevents.R;
import com.example.musicalevents.adminUser.AdminActivity;
import com.example.musicalevents.base.Event;
import com.example.musicalevents.data.model.Userkt;
import com.example.musicalevents.databinding.ActivityLoginBinding;
import com.example.musicalevents.normalUser.MainActivity;
import com.example.musicalevents.signup.SignUpActivity;
import com.example.musicalevents.utils.CommonUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {
    Userkt e1 = new Userkt();
    private ActivityLoginBinding binding;
    private LoginContract.Presenter presenter;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setTitle(R.string.tvLogin_login);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.

        binding.btSignUp.setOnClickListener(view -> startActivityLogin());


        binding.btSignIn.setOnClickListener(view -> {
                    e1.setEmail(binding.tieEmail.getText().toString());
                    e1.setPassword(binding.tiePassword.getText().toString());
                    presenter.validateCredentials(e1);
                }
        );

        presenter = new LoginPresenter(this);
        //la vista se registra como subscriptor del EventBus
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Se evitaria un futuro memory leaks
        presenter.onDestroy();
        //Se quita como subcriptor del EventBus
        EventBus.getDefault().unregister(this);
    }


    private void startActivityLogin() {
        startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
    }


    //region Metodos del contrato LoginContract.View

    /**
     * Este metodo activa el error en el componente TextInputLayout y mostrar el texto oportuno
     */
    @Override
    public void setEmailEmptyError() {
        binding.tilEmail.setError(getString(R.string.error_EmailEmpty));
    }

    @Override
    public void setPasswordEmptyError() {
        binding.tilPassword.setError(getString(R.string.errorPasswordEmpty));
    }

    @Override
    public void setPasswordError() {
        binding.tilPassword.setError(getString(R.string.error_passwordFormat));
    }

    @Override
    public void showProgress() {
        //binding.progressHorizontal.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        //binding.progressHorizontal.setVisibility(View.INVISIBLE);
    }


    //endregion


    //region Metodos del contrato con LoginContract.View extend OnLoginListener es decir son los metodos que obtiene por herencia de la otra interfaz

    /**
     * El usuario existe en la base de datos, usuario contraseña correctos
     */
    @Override
    public void onSuccess(Userkt e) {
//        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
////        editor.putBoolean("isAdmin", e.isAdmin());
////        editor.apply();
//        if (binding.chkRemember.isChecked()) {
//            editor.putString(Userkt.TAG, binding.tieEmail.getText().toString());
//            editor.apply();
//            //O BIEN APPLY O BIEN COMMIT QUE SINO NO SE HACEN LOS CAMBIOS EN EK FICHERO
//
//        }
//

        if (Boolean.TRUE.equals(e.isAdmin())) {
            //Carga una vista
            startAdminActivity();
        } else if (Boolean.FALSE.equals(e.isAdmin())) {
            //Carga otra vista
            startMainActivity();
        }

    }

    private void startAdminActivity() {
        startActivity(new Intent(LoginActivity.this, AdminActivity.class));
    }

    private void startMainActivity() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }

    @Override
    public void onFailure(int message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    //endregion


    //region Clase interna que controla cada vez que el usuario introduce un caracter en un editable
    // TExtInputLayout si cumple o no las reglas de negocio

    /**
     * Metodo que valida que la contraseña mediante el metodo ya creado  en la clase CommonUtils
     *
     * @param password
     */
    private void validatePassword(String password) {

        if (TextUtils.isEmpty(password)) {
            binding.tilPassword.setError(getString(R.string.errorPasswordEmpty));
        } else if (!CommonUtils.isPasswordValid(password)) {
            binding.tilPassword.setError(getString(R.string.error_passwordFormat));
        } else {
            //desaparece el error
            binding.tilPassword.setError(null);
        }
    }

    /**
     * Metodo aue valida el email
     * 1.- no puede ser vacio
     * 2.- vamos a utilzar el patron por defecto de email para comprobar que es correcto
     *
     * @param email
     */
    private void validateEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            binding.tilEmail.setError(getString(R.string.error_EmailEmpty));
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.setError(getString(R.string.error_email));
        } else {
            //desaparece el error
            binding.tilEmail.setError(null);
        }
    }

    @Subscribe
    public void onEvent(Event event) {
        hideProgress();
        Toast.makeText(this, event.getMessage(), Toast.LENGTH_SHORT).show();

    }


}