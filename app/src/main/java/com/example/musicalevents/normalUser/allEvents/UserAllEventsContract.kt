package com.example.musicalevents.normalUser.allEvents

import com.example.musicalevents.data.model.Event

interface UserAllEventsContract {
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