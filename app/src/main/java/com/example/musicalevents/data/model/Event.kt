package com.example.musicalevents.data.model

import com.example.musicalevents.data.repository.LoginRepository
import java.io.Serializable
import java.util.*

data class Event  @JvmOverloads constructor(
    val uuid: String = UUID.randomUUID().toString(),
    val nombreEvento: String = "",
    val user : Userkt = LoginRepository.currentUser,
    val ubicacion: String = "",
    val fechaInicioMiliSegundos: Long = 0L,
    val fechaFinMiliSegundos: Long = 0L,
    val descripcion : String = ""
) : Serializable{
}
