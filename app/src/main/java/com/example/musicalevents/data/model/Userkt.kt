package com.example.musicalevents.data.model


data class Userkt @JvmOverloads constructor(
    var email: String? = null,
    var password: String? = null,
    var isAdmin: Boolean = false
) {
    companion object {
        const val TAG = "user"
    }
}