package com.example.musicalevents.adminUser.allEvents

import com.example.musicalevents.data.model.Event

interface AllEventsContract {
    interface View : OnRepositoryCallback {
        fun showProgress()
        fun hideProgress()
    }

    interface Presenter : OnRepositoryCallback {
        fun getAllEvents(year : String, month : String, day: String)
        fun onDestroy()
    }

    interface Interactor : OnRepositoryCallback {
        fun getAllEvents(year : String, month : String, day: String)
    }

    interface OnRepositoryCallback {
        fun onListSuccess(eventList: List<Event>)
        fun onNoData()
    }
}