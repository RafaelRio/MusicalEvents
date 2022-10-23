package com.example.musicalevents.data.model

import com.example.musicalevents.data.repository.LoginRepository
import java.io.Serializable

data class Event  @JvmOverloads constructor(
    var nombreEvento: String? = null,
    var user : String? = LoginRepository.currentUser.email,
    var ubicacion: String? = null,
    var dia: Int? = null,
    var mes : Int? = null,
    var anio : Int? = null,
    var horaComienzo : String? = null,
    var horaFin : String? = null,
    var descripcion : String? = null
) : Serializable{
}
