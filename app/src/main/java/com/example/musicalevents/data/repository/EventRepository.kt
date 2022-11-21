package com.example.musicalevents.data.repository

import com.example.musicalevents.data.model.Event
import com.example.musicalevents.mvp.allevents.AllEventsContract
import com.example.musicalevents.mvp.uploadedevents.UploadedEventsContract
import com.example.musicalevents.utils.UtilsKt.Companion.eventosTable
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class EventRepository : UploadedEventsContract.Repository {
    companion object{
        private const val TAG = "JAVAREPOSITORY"
        val instance: EventRepository = EventRepository()
    }

    fun getInstance(): EventRepository {
        return instance
    }

    private val db = FirebaseFirestore.getInstance()
    private val currentUser = LoginRepository.currentUser

    fun getMyEvents(callback: UploadedEventsContract.OnRepositoryCallback) {
        val misEventos: MutableList<Event> = ArrayList()
        db.collection(eventosTable)
            .whereEqualTo("user.email", currentUser.email)
            .get()
            .addOnCompleteListener { task: Task<QuerySnapshot> ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        misEventos.add(document.toObject(Event::class.java))
                        callback.onListSuccess(misEventos)
                    }
                    if (misEventos.size == 0) {
                        callback.onNoData()
                    }
                }
            }
    }

    fun getAllEvents(callback: AllEventsContract.OnRepositoryCallback, fechaUnix: Long?) {
        val allEvents: MutableList<Event> = ArrayList()
        db.collection(eventosTable)
            .get()
            .addOnCompleteListener { task: Task<QuerySnapshot> ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        val df: DateFormat = SimpleDateFormat("dd/MM/yyyy")
                        val cal = Calendar.getInstance()
                        cal.timeInMillis = fechaUnix!!
                        val fechaSeleccionada = df.format(cal.time)
                        val (_, _, _, _, fechaInicioMiliSegundos) = document.toObject(
                            Event::class.java
                        )
                        val eventDate = Calendar.getInstance()
                        eventDate.timeInMillis = fechaInicioMiliSegundos
                        val fechaEvento = df.format(eventDate.time)
                        if (fechaEvento == fechaSeleccionada) {
                            allEvents.add(document.toObject(Event::class.java))
                        }
                    }
                    callback.onListSuccess(allEvents)
                    if (allEvents.isEmpty()) {
                        callback.onNoData()
                    }
                }
            }
    }

    override fun delete(callback: UploadedEventsContract.OnRepositoryCallback?, event: Event) {
        db.collection(eventosTable).document(Objects.requireNonNull(event.uuid)).delete()
        assert(callback != null)
        callback!!.onDeleteSuccess(event)
    }

    fun uploadEvent(uploadedEvent: Event) {
        db.collection(eventosTable)
            .document(uploadedEvent.uuid)
            .set(uploadedEvent)
    }
}