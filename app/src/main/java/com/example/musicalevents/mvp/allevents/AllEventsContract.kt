package com.example.musicalevents.mvp.allevents

import com.example.musicalevents.data.model.Event

/**
 * Contrato para MVP de todos lo eventos subidos a la base de datos
 * @author Rafa
 */
interface AllEventsContract {
    interface View : OnRepositoryCallback {
    }

    interface Presenter : OnRepositoryCallback {
        fun getAllEvents(fechaUnix: Long)
        fun onDestroy()
    }

    interface Interactor : OnRepositoryCallback {
        fun getAllEvents(fechaUnix: Long)
    }

    interface OnRepositoryCallback {
        fun onListSuccess(eventList: List<Event>)
        fun onNoData()
    }
}