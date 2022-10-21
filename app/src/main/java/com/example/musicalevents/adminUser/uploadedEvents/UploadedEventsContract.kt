package com.example.musicalevents.adminUser.uploadedEvents

import com.example.musicalevents.data.model.Event

interface UploadedEventsContract {

    interface View : OnRepositoryCallback {
        fun showProgress()
        fun hideProgress()
    }

    interface Presenter : OnRepositoryCallback {
        fun delete(event: Event)
        fun getMyEvents()
        fun onDestroy()
    }

    interface Interactor : OnRepositoryCallback {
        fun delete(event: Event)
        fun getMyEvents()
    }

    interface OnRepositoryCallback {
        fun onListSuccess(eventList: List<Event>)
        fun onDeleteSuccess(deletedEvent : Event)
        fun onNoData()
    }

    interface Repository {
        fun delete(callback: OnRepositoryCallback?, event: Event)
    }
}