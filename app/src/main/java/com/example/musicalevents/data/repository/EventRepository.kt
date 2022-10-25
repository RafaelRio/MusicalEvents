package com.example.musicalevents.data.repository

import com.example.musicalevents.data.model.Event
import com.example.musicalevents.data.model.Userkt
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*


class EventRepository {

    private val db = FirebaseFirestore.getInstance()
    private var currentUser: Userkt = LoginRepository.currentUser
    private val myEvents = ArrayList<Event>()
    private var uuidEvent : UUID = UUID.randomUUID()

    companion object {
        private lateinit var instance: EventRepository
    }

    fun getInstance(): EventRepository {
        instance = EventRepository()
        return instance
    }

    fun uploadEvent(uploadedEvent: Event) {
        uploadedEvent.uuid = uuidEvent.toString()
        db.collection("eventos")
            .document(uuidEvent.toString())
            .set(uploadedEvent)
    }
}
