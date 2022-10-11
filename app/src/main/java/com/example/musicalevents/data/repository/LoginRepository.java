package com.example.musicalevents.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.musicalevents.base.OnRepositoryCallback;
import com.example.musicalevents.data.model.Userkt;
import com.example.musicalevents.login.LoginContract;
import com.example.musicalevents.signup.SignUpContract;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class LoginRepository implements LoginContract.Repository, SignUpContract.Repository {
    private static final String TAG = "AAAAAAAAAAA";
    private static LoginRepository instance;
    private OnRepositoryCallback callback;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();


    public static LoginRepository getInstance(OnRepositoryCallback listener) {
        if (instance == null) {
            instance = new LoginRepository();
        }
        instance.callback = listener;
        return instance;
    }

    @Override
    public void login(Userkt user) {

        db.collection("personas").document(user.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            Userkt databaseUser = task.getResult().toObject(Userkt.class);
                            if (databaseUser == null) {
                                callback.onFailure("Usuario incorrecto");
                            }else if (Objects.equals(user.getPassword(), databaseUser.getPassword())) {
                                callback.onSuccess(databaseUser);
                            }
                            else {
                                callback.onFailure("Usuario incorrecto");
                            }
                        }
                    }
                });
    }

    @Override
    public void SignUp(String user, String email, String password, String comfirmPassword) {
        Userkt databaseUser = new Userkt(email, password, false);
        db.collection("personas").document(databaseUser.getEmail())
                .set(databaseUser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        callback.onSuccess(databaseUser);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }
}
