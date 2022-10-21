package com.example.musicalevents.adminUser.allEvents

import com.example.musicalevents.data.model.Event

interface AllEventsContract {
    interface View : OnRepositoryCallback {
        fun showProgress()
        fun hideProgress()
    }

    interface Presenter : OnRepositoryCallback {
        fun getAllEvents(year : Int, month : Int, day: Int)
        fun onDestroy()
    }

    interface Interactor : OnRepositoryCallback {
        fun getAllEvents(year : Int, month : Int, day: Int)
    }

    interface OnRepositoryCallback {
        fun onListSuccess(eventList: List<Event>)
        fun onNoData()
    }
}