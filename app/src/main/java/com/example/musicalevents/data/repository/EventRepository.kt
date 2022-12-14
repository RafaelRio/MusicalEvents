package com.example.musicalevents.data.repository

import com.example.musicalevents.data.model.Event
import com.example.musicalevents.mvp.allevents.AllEventsContract
import com.example.musicalevents.mvp.uploadedevents.UploadedEventsContract
import com.example.musicalevents.utils.UtilsKt.eventosTable
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.util.*

/**
 * Repositorio que conecta con la base de datos para hacer operaciones con los eventos
 * @author Rafa
 */

class EventRepository : UploadedEventsContract.Repository {
    companion object {
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

    fun getAllEvents(callback: AllEventsContract.OnRepositoryCallback, fechaUnix: Long) {
        val allEvents: MutableList<Event> = ArrayList()
        db.collection(eventosTable)
            .get()
            .addOnCompleteListener { task: Task<QuerySnapshot> ->
                if (task.isSuccessful) {
                    for (document in task.result) {

                        var cal = Calendar.getInstance().apply { timeInMillis = fechaUnix }
                        val selectedYear = cal.get(Calendar.YEAR)
                        val selectedMonth = cal.get(Calendar.MONTH)
                        val selectedDay = cal.get(Calendar.DAY_OF_MONTH)

                       val event = document.toObject(Event::class.java)

                        cal = Calendar.getInstance().apply { timeInMillis = event.startDate }
                        val eventYear = cal.get(Calendar.YEAR)
                        val eventMonth = cal.get(Calendar.MONTH)
                        val eventDay = cal.get(Calendar.DAY_OF_MONTH)

                        val areDatesEqual = (selectedYear == eventYear) && (selectedMonth == eventMonth) && (selectedDay == eventDay)

                        if (areDatesEqual) {
                            allEvents.add(event)
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