package com.example.musicalevents.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.musicalevents.adminUser.allEvents.AllEventsContract;
import com.example.musicalevents.adminUser.uploadedEvents.UploadedEventsContract;
import com.example.musicalevents.data.model.Event;
import com.example.musicalevents.data.model.Userkt;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JavaEventRepository implements UploadedEventsContract.Repository {

    private static final String TAG = "JAVAREPOSITORY";
    private static JavaEventRepository instance;

    static {
        instance = new JavaEventRepository();
    }

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Userkt currentUser = LoginRepository.currentUser;

    public static JavaEventRepository getInstance() {
        if (instance == null) {
            instance = new JavaEventRepository();
        }
        return instance;
    }

    public void getMyEvents(UploadedEventsContract.OnRepositoryCallback callback) {
        final List<Event> misEventos = new ArrayList<>();

        db.collection("eventos")
                .whereEqualTo("user", currentUser.getEmail())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            misEventos.add(document.toObject(Event.class));
                            callback.onListSuccess(misEventos);
                        }
                        if (misEventos.size() == 0) {
                            callback.onNoData();
                        }
                    } else {
                        Log.d(TAG, "Error onteniendo mis documentos", task.getException());

                    }
                });
    }

    public void getAllEvents(AllEventsContract.OnRepositoryCallback callback, int year, int month, int day) {
        final List<Event> allEvents = new ArrayList<>();

        db.collection("eventos").
                whereEqualTo("dia", day).
                whereEqualTo("mes", month).
                whereEqualTo("anio", year)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            allEvents.add(document.toObject(Event.class));
                            callback.onListSuccess(allEvents);
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                        callback.onNoData();
                    }
                });

    }

    @Override
    public void delete(UploadedEventsContract.OnRepositoryCallback callback, Event deletedEvent) {
        /*db.collection("eventos").document(deletedEvent.getNombreEvento().trim() + deletedEvent.getDescripcion().trim() +
                        deletedEvent.getUbicacion().trim() + deletedEvent.getDescripcion().trim() + deletedEvent.getUser().trim() +
                        deletedEvent.getDia().toString().trim() + deletedEvent.getAnio().toString().trim() + deletedEvent.getMes().toString().trim()).
                delete();*/
        db.collection("eventos").document(Objects.requireNonNull(deletedEvent.getUuid())).delete();
        assert callback != null;
        callback.onDeleteSuccess(deletedEvent);
    }
}
