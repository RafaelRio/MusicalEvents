package com.example.musicalevents.data.model


data class Userkt @JvmOverloads constructor(
    var email: String? = null,
    var password: String? = null,
    var isAdmin: Boolean = false,
) {
    companion object {
        const val TAG = "user"
    }
}

/*
* Diferencias entre parametro y propiedad
*
* En el constructor principal en kt puedo definir propiedades o parametros
* Las propiedades se definen con var o val y pertenencen a la clase
* Los parametros se definen sin nada delante y no pertenecen a la clase
* */