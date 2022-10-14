package com.example.musicalevents.data.repository

import android.util.Log
import com.example.musicalevents.data.model.Event
import com.example.musicalevents.data.model.Userkt
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore

class EventRepository {

    var eventList: ArrayList<Event>? = null
    private val db = FirebaseFirestore.getInstance()
    private var currentUser : Userkt = LoginRepository.currentUser

    companion object{
        private lateinit var instance: EventRepository
    }
    fun getInstance(): EventRepository {
        instance = EventRepository()
        return instance
    }

    fun uploadEvent(uploadedEvent : Event){
        db.collection("eventos").document(uploadedEvent.nombreEvento+uploadedEvent.descripcion+uploadedEvent.ubicacion).set(uploadedEvent)
        /*currentUser.email?.let { db.collection("personas").document(it).set(currentUser).addOnCompleteListener(
            OnSuccessListener<Void?> {
                Log.d(LoginRepository.TAG, "DocumentSnapshot successfully written!")
                callback.onSuccess(databaseUser)
            })
            .addOnFailureListener { e ->
                    Log.w(
                        LoginRepository.TAG,
                        "Error writing document",
                        e
                    )
                }
        }*/
    }
}
