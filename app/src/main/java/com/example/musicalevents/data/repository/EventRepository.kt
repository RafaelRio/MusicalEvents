package com.example.musicalevents.data.repository

import android.util.Log
import com.example.musicalevents.data.model.Event
import com.example.musicalevents.data.model.Userkt
import com.example.musicalevents.data.model.Userkt.Companion.TAG
import com.google.firebase.firestore.FirebaseFirestore


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
            .document(uploadedEvent.nombreEvento + uploadedEvent.descripcion + uploadedEvent.ubicacion)
            .set(uploadedEvent)
        currentUser.uploadedEvents.add(uploadedEvent)
        currentUser.email?.let { db.collection("personas").document(it).set(currentUser) }
    }

    fun myEvents(): ArrayList<Event> {
        db.collection("personas").whereEqualTo("email", currentUser.email)
            .get().addOnSuccessListener { documents ->
                //ToDo rellenar el array myEvents desde aqui
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
        return myEvents
    }
}
