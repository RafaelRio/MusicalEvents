package com.example.musicalevents.data.model

data class Event @JvmOverloads constructor(
    var nombreEvento: String? = null,
    var ubicacion: String? = null,
    var dia: String? = null,
    var mes : String? = null,
    var anio : String? = null,
    var horaComienzo : String? = null,
    var descripcion : String? = null
) {

}
