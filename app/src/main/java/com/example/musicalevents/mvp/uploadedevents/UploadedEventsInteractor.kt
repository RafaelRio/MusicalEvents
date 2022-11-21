package com.example.musicalevents.mvp.uploadedevents

import com.example.musicalevents.data.model.Event
import com.example.musicalevents.data.repository.EventRepository

class UploadedEventsInteractor(private var listener: UploadedEventsContract.OnRepositoryCallback?) :
    UploadedEventsContract.Interactor {

    override fun delete(event: Event) {
        EventRepository.instance.delete(this, event)
    }

    override fun getMyEvents() {
        EventRepository.instance.getMyEvents(this)
    }

    override fun onListSuccess(eventList: List<Event>) {
        listener?.onListSuccess(eventList)
    }

    override fun onDeleteSuccess(deletedEvent: Event) {
        listener?.onDeleteSuccess(deletedEvent)
    }

    override fun onEditSuccess(editedEvent: Event) {
        listener?.onEditSuccess(editedEvent)
    }

    override fun onNoData() {
        listener?.onNoData()
    }
}