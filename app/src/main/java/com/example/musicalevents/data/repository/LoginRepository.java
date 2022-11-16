package com.example.musicalevents.data.repository;


import android.util.Log;

import com.example.musicalevents.R;
import com.example.musicalevents.base.OnRepositoryCallback;
import com.example.musicalevents.data.model.Userkt;
import com.example.musicalevents.login.LoginContractKt;
import com.example.musicalevents.signup.SignUpContractKt;
import com.example.musicalevents.utils.UtilsKt;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class LoginRepository implements LoginContractKt.Repository, SignUpContractKt.Repository {
    private static final String TAG = "AAAAAAAAAAA";
    public static Userkt currentUser;
    private static LoginRepository instance;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private OnRepositoryCallback callback;


    public static LoginRepository getInstance(OnRepositoryCallback listener) {
        if (instance == null) {
            instance = new LoginRepository();
        }
        instance.callback = listener;
        return instance;
    }

    @Override
    public void login(Userkt user) {

        db.collection(UtilsKt.Companion.getPersonasTable()).document(Objects.requireNonNull(user.getEmail()))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Userkt databaseUser = task.getResult().toObject(Userkt.class);
                        if (databaseUser == null) {
                            callback.onFailure(R.string.error_login);
                        } else if (Objects.equals(user.getPassword(), databaseUser.getPassword())) {
                            currentUser = databaseUser;
                            callback.onSuccess(databaseUser);
                        } else {
                            callback.onFailure(R.string.error_login);
                        }
                    }
                });
    }

    @Override
    public void SignUp(String user, String email, String password, String comfirmPassword) {
        Userkt databaseUser = new Userkt(email, password, false);
        ArrayList<String> usedEmails = new ArrayList<>();

        db.collection(UtilsKt.Companion.getPersonasTable())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            usedEmails.add(document.getId());
                        }
                        if (!usedEmails.contains(databaseUser.getEmail())){
                            createUser(user, email, password, comfirmPassword);
                        }else {
                            callback.onFailure(R.string.error_userExists);
                        }
                    }
                });
    }

    public void createUser(String user, String email, String password, String comfirmPassword) {
        Userkt databaseUser = new Userkt(email, password, false);
        db.collection("personas").document(Objects.requireNonNull(databaseUser.getEmail()))
                .set(databaseUser)
                .addOnSuccessListener(aVoid -> {
                    callback.onSuccess(databaseUser);
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));
    }
}