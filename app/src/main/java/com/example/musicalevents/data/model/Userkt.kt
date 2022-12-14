package com.example.musicalevents.data.model

import java.io.Serializable

/**
 * Clase objeto de los usuarios
 * @author Rafa
 */

data class Userkt @JvmOverloads constructor(
    var name: String? = null,
    var email: String? = null,
    var password: String? = null,
    var isAdmin: Boolean = false,
    var twitter: String? = "",
    var instagram: String? = "",
    var facebook: String? = "",
    var website: String? = ""
) : Serializable {
}