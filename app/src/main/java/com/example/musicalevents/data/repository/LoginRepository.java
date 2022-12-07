package com.example.musicalevents.data.repository;


import com.example.musicalevents.R;
import com.example.musicalevents.base.OnRepositoryCallback;
import com.example.musicalevents.data.model.Userkt;
import com.example.musicalevents.mvp.login.LoginContractKt;
import com.example.musicalevents.mvp.signup.SignUpContractKt;
import com.example.musicalevents.utils.UtilsKt;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.okhttp.internal.Util;

import java.util.ArrayList;
import java.util.Objects;

public class LoginRepository implements LoginContractKt.Repository, SignUpContractKt.Repository {
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
        db.collection(UtilsKt.personasTable).document(Objects.requireNonNull(user.getEmail()))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Userkt databaseUser = task.getResult().toObject(Userkt.class);
                        if (databaseUser == null) {
                            callback.onFailure(R.string.error_login);
                        } else if (Objects.equals(Util.shaBase64(Objects.requireNonNull(user.getPassword())), databaseUser.getPassword())) {
                            currentUser = databaseUser;
                            callback.onSuccess(databaseUser);
                        } else {
                            callback.onFailure(R.string.error_login);
                        }
                    }
                });
    }

    @Override
    public void SignUp(String name, String email, String password, String confirmPassword) {
        Userkt databaseUser = new Userkt(name, email, password, false);
        ArrayList<String> usedEmails = new ArrayList<>();

        db.collection(UtilsKt.personasTable)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            usedEmails.add(document.getId());
                        }
                        if (!usedEmails.contains(databaseUser.getEmail())){
                            createUser(name, email, password);
                        }else {
                            callback.onFailure(R.string.error_userExists);
                        }
                    }
                });
    }

    public void createUser(String name, String email, String password) {
        Userkt databaseUser = new Userkt(name, email, Util.shaBase64(password), false);
        db.collection(UtilsKt.personasTable).document(Objects.requireNonNull(databaseUser.getEmail()))
                .set(databaseUser)
                .addOnSuccessListener(aVoid -> {
                    callback.onSuccess(databaseUser);
                });
    }
}