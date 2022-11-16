package com.example.musicalevents.data.repository;

import android.util.Log;

import com.example.musicalevents.adminUser.allEvents.AllEventsContract;
import com.example.musicalevents.adminUser.uploadedEvents.UploadedEventsContract;
import com.example.musicalevents.data.model.Event;
import com.example.musicalevents.data.model.Userkt;
import com.example.musicalevents.normalUser.allEvents.UserAllEventsContract;
import com.example.musicalevents.utils.UtilsKt;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class JavaEventRepository implements UploadedEventsContract.Repository {

    private static final String TAG = "JAVAREPOSITORY";
    private static JavaEventRepository instance;

    static {
        instance = new JavaEventRepository();
    }

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final Userkt currentUser = LoginRepository.currentUser;

    public static JavaEventRepository getInstance() {
        if (instance == null) {
            instance = new JavaEventRepository();
        }
        return instance;
    }

    public void getMyEvents(UploadedEventsContract.OnRepositoryCallback callback) {
        final List<Event> misEventos = new ArrayList<>();

        db.collection(UtilsKt.Companion.getEventosTable())
                .whereEqualTo("user.email", currentUser.getEmail())
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

    public void getAllEvents(AllEventsContract.OnRepositoryCallback callback, Long fechaUnix) {
        final List<Event> allEvents = new ArrayList<>();

        db.collection(UtilsKt.Companion.getEventosTable())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                            Calendar cal = Calendar.getInstance();
                            cal.setTimeInMillis(fechaUnix);
                            String fechaSeleccionada = df.format(cal.getTime());

                            Event evento = document.toObject(Event.class);
                            Calendar eventDate = Calendar.getInstance();
                            eventDate.setTimeInMillis(evento.getFechaInicioMiliSegundos());
                            String fechaEvento = df.format(eventDate.getTime());
                            if (fechaEvento.equals(fechaSeleccionada)) {
                                allEvents.add(document.toObject(Event.class));
                            }
                        }
                        callback.onListSuccess(allEvents);
                        if (allEvents.isEmpty()) {
                            callback.onNoData();
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });

    }


    //ToDo Cambiar este metodo para ser igual al de arriba
    public void userGetAllEvents(UserAllEventsContract.OnRepositoryCallback callback, Long fechaUnix) {
        final List<Event> allEvents = new ArrayList<>();

        db.collection(UtilsKt.Companion.getEventosTable())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                            Calendar cal = Calendar.getInstance();
                            cal.setTimeInMillis(fechaUnix);
                            String fechaSeleccionada = df.format(cal.getTime());

                            Event evento = document.toObject(Event.class);
                            Calendar eventDate = Calendar.getInstance();
                            eventDate.setTimeInMillis(evento.getFechaInicioMiliSegundos());
                            String fechaEvento = df.format(eventDate.getTime());
                            if (fechaEvento.equals(fechaSeleccionada)) {
                                allEvents.add(document.toObject(Event.class));
                            }
                        }
                        callback.onListSuccess(allEvents);
                        if (allEvents.isEmpty()) {
                            callback.onNoData();
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

    @Override
    public void delete(UploadedEventsContract.OnRepositoryCallback callback, Event deletedEvent) {
        db.collection(UtilsKt.Companion.getEventosTable()).document(Objects.requireNonNull(deletedEvent.getUuid())).delete();
        assert callback != null;
        callback.onDeleteSuccess(deletedEvent);
    }

    public void uploadEvent(Event uploadedEvent) {
        db.collection(UtilsKt.Companion.getEventosTable())
                .document(uploadedEvent.getUuid())
                .set(uploadedEvent);
    }
}
