package com.example.musicalevents.adminUser.uploadedEvents

import com.example.musicalevents.data.model.Event
import com.example.musicalevents.data.repository.JavaEventRepository

class UploadedEventsInteractor(private var listener: UploadedEventsContract.OnRepositoryCallback?) : UploadedEventsContract.Interactor {

    override fun delete(event: Event) {
        JavaEventRepository.getInstance().delete(this, event)
    }

    override fun getMyEvents() {
        JavaEventRepository.getInstance().getMyEvents(this)
    }

    override fun onListSuccess(eventList: List<Event>) {
        listener?.onListSuccess(eventList)
    }

    override fun onDeleteSuccess(deletedEvent: Event) {
        listener?.onDeleteSuccess(deletedEvent)
    }

    override fun onNoData() {
        listener?.onNoData()
    }
}