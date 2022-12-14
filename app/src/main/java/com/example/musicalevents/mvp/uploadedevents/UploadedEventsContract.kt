package com.example.musicalevents.mvp.uploadedevents

import com.example.musicalevents.data.model.Event

/**
 * Contrato de mis eventos
 * @author Rafa
 */
interface UploadedEventsContract {

    interface View : OnRepositoryCallback {
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
        fun onEditSuccess(editedEvent : Event)
        fun onNoData()
    }

    interface Repository {
        fun delete(callback: OnRepositoryCallback?, event: Event)
    }
}