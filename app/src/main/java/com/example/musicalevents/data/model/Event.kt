package com.example.musicalevents.data.model

import com.example.musicalevents.data.repository.LoginRepository
import java.io.Serializable
import java.util.*

data class Event  @JvmOverloads constructor(
    var uuid: String? = null,
    var nombreEvento: String? = null,
    var user : Userkt? = LoginRepository.currentUser,
    var ubicacion: String? = null,
    var diaInicio: String? = null,
    var mesInicio : String? = null,
    var anioInicio : String? = null,
    var horaComienzo : String? = null,
    var diaFin: String? = null,
    var mesFin : String? = null,
    var anioFin : String? = null,
    var horaFin : String? = null,
    var descripcion : String? = null
) : Serializable{
}
