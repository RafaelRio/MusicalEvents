package com.example.musicalevents.data.repository;


import android.util.Log;

import androidx.annotation.NonNull;

import com.example.musicalevents.base.OnRepositoryCallback;
import com.example.musicalevents.data.model.Userkt;
import com.example.musicalevents.login.LoginContract;
import com.example.musicalevents.signup.SignUpContract;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class LoginRepository implements LoginContract.Repository, SignUpContract.Repository {
    private static final String TAG = "AAAAAAAAAAA";
    public static Userkt currentUser;
    private static LoginRepository instance;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private OnRepositoryCallback callback;
    private UUID uuidUser = UUID.randomUUID();


    public static LoginRepository getInstance(OnRepositoryCallback listener) {
        if (instance == null) {
            instance = new LoginRepository();
        }
        instance.callback = listener;
        return instance;
    }

    @Override
    public void login(Userkt user) {

        db.collection("personas").document(Objects.requireNonNull(user.getEmail()))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Userkt databaseUser = task.getResult().toObject(Userkt.class);
                        if (databaseUser == null) {
                            callback.onFailure("Usuario incorrecto");
                        } else if (Objects.equals(user.getPassword(), databaseUser.getPassword())) {
                            currentUser = databaseUser;
                            callback.onSuccess(databaseUser);
                        } else {
                            callback.onFailure("Usuario incorrecto");
                        }
                    }
                });
    }

    @Override
    public void SignUp(String user, String email, String password, String comfirmPassword) {
        Userkt databaseUser = new Userkt(email, password, false);
        ArrayList<String> usedEmails = new ArrayList<>();

        db.collection("personas")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            usedEmails.add(document.getId());
                        }
                        if (!usedEmails.contains(databaseUser.getEmail())){
                            createUser(user, email, password, comfirmPassword);
                        }else {
                            callback.onFailure("El usuario ya existe");
                        }
                    }
                });
    }

    public void createUser(String user, String email, String password, String comfirmPassword) {
        Userkt databaseUser = new Userkt(email, password, false);
        db.collection("personas").document(Objects.requireNonNull(databaseUser.getEmail()))
                .set(databaseUser)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "DocumentSnapshot successfully written!");
                    callback.onSuccess(databaseUser);
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));
    }
}