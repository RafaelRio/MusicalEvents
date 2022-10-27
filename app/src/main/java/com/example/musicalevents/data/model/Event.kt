package com.example.musicalevents.data.model

import com.example.musicalevents.data.repository.LoginRepository
import java.io.Serializable
import java.util.*

data class Event  @JvmOverloads constructor(
    var uuid: String? = null,
    var nombreEvento: String? = null,
    var user : String? = LoginRepository.currentUser.email,
    var ubicacion: String? = null,
    var diaComienzo: Int? = null,
    var mesComienzo : Int? = null,
    var anioComienzo : Int? = null,
    var horaComienzo : String? = null,
    var diaFin: Int? = null,
    var mesFin : Int? = null,
    var anioFin : Int? = null,
    var horaFin : String? = null,
    var descripcion : String? = null
) : Serializable{
}
