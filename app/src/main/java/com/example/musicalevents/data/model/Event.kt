package com.example.musicalevents.data.model

import com.example.musicalevents.data.repository.LoginRepository
import java.io.Serializable
import java.util.*

//ToDo Crear una pantalla de perfil para cambiar nombre y contraseña (opcional) y que salga el correo pero que no se pueda modificar
//ToDo Pantalla de ajustes para cambiar el idioma
//ToDo Que el item de evento tenga una foto de la agrupacion que la ha subido, si no tiene saldrá una por defecto
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
