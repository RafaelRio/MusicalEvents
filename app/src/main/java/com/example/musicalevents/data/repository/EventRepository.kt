package com.example.musicalevents.data.repository

import com.example.musicalevents.adminUser.uploadedEvents.UploadedEventsContract
import com.example.musicalevents.data.model.Event
import com.example.musicalevents.data.model.Userkt
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.ExecutionException


class EventRepository {

    private val db = FirebaseFirestore.getInstance()
    private var currentUser: Userkt = LoginRepository.currentUser
    private val myEvents = ArrayList<Event>()

    companion object {
        private lateinit var instance: EventRepository
    }

    fun getInstance(): EventRepository {
        instance = EventRepository()
        return instance
    }

    fun uploadEvent(uploadedEvent: Event) {
        db.collection("eventos")
            .document(uploadedEvent.nombreEvento?.trim() + uploadedEvent.descripcion?.trim() +
                    uploadedEvent.ubicacion?.trim() + uploadedEvent.descripcion?.trim() + uploadedEvent.user?.trim() +
                    uploadedEvent.dia.toString().trim() + uploadedEvent.anio.toString().trim() + uploadedEvent.mes.toString().trim())
            .set(uploadedEvent)
    }

    fun deleteEvent(deletedEvent : Event){
        db.collection("eventos")
            .document(deletedEvent.nombreEvento?.trim() + deletedEvent.descripcion?.trim() +
                    deletedEvent.ubicacion?.trim() + deletedEvent.descripcion?.trim() + deletedEvent.user?.trim() +
                    deletedEvent.dia.toString().trim() + deletedEvent.anio.toString().trim() + deletedEvent.mes.toString().trim()).
                delete()
    }
}
