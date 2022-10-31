package com.example.musicalevents.signup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.musicalevents.R;
import com.example.musicalevents.base.Event;
import com.example.musicalevents.data.model.Userkt;
import com.example.musicalevents.databinding.ActivitySignUpBinding;
import com.example.musicalevents.login.LoginActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class SignUpActivity extends AppCompatActivity implements SignUpContract.View {

    ActivitySignUpBinding binding;
    private SignUpContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle(R.string.tvTitleSignUp);


//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding.btAlreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateUpTo(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });

        /**
         * Se utiliza el metodo on backpressed para elemiminar la activivity signUpActivity y restaurar
         * la actividad anterior LoginActivity
         */
        binding.btRegistrar.setOnClickListener(view -> presenter.validateSignUp(binding.tieUser.getText().toString(),
                binding.tieEmail.getText().toString(),
                binding.tiePassword.getText().toString(),
                binding.tieConfirmPassword.getText().toString()));
        presenter = new SignUpPresenter(this);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Se evitaria un futuro memory leaks
        presenter.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void setUserEmptyError() {
        binding.tilUser.setError(getString(R.string.errUserEmpty));
    }

    @Override
    public void setEmailEmptyError() {
        binding.tilEmail.setError(getString(R.string.error_EmailEmpty));
    }

    @Override
    public void setPasswordEmptyError() {
        binding.tilPassword.setError(getString(R.string.errorPasswordEmpty));
    }

    @Override
    public void setConfirmPasswordEmptyError() {
        binding.tilConfirmPassword.setError(getString(R.string.errorPasswordEmpty));
    }

    @Override
    public void setPasswordError() {
        binding.tilPassword.setError(getString(R.string.error_passwordFormat));
    }

    @Override
    public void setEmailError() {
        binding.tilEmail.setError(getString(R.string.error_email));
    }

    @Override
    public void setPasswordDontMatch() {
        Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onSuccess(Userkt e) {
        finish();
    }

    @Override
    public void onFailure(int message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Subscribe
    public void onEvent(Event event) {
        hideProgress();
        Toast.makeText(this, event.getMessage(), Toast.LENGTH_SHORT).show();

    }
}