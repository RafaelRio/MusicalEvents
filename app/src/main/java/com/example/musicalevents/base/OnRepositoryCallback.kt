package com.example.musicalevents.base

import com.example.musicalevents.data.model.Userkt

/**
 * Interfaz base para todos los que reciban una respuesta del repositorio
 * @author Rafa
 */

interface OnRepositoryCallback {
    fun onSuccess(u: Userkt)
    fun onFailure(message: Int)
}