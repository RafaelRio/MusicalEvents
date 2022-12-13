package com.example.musicalevents.data.model

import com.example.musicalevents.data.repository.LoginRepository
import java.io.Serializable
import java.util.*

data class Event  @JvmOverloads constructor(
    val uuid: String = UUID.randomUUID().toString(),
    val eventName: String = "",
    val user : Userkt = LoginRepository.currentUser,
    val location: String = "",
    val startDate: Long = 0L,
    val endDate: Long = 0L,
    val description : String = "",
    var lat : Double = 0.0,
    var lon : Double = 0.0
) : Serializable{
}
