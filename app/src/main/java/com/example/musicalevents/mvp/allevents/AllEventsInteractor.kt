package com.example.musicalevents.mvp.allevents

import com.example.musicalevents.data.model.Event
import com.example.musicalevents.data.repository.EventRepository

class AllEventsInteractor(private var listener: AllEventsContract.OnRepositoryCallback?) :
    AllEventsContract.Interactor {

    override fun getAllEvents(fechaUnix: Long) {
        EventRepository.instance.getAllEvents(this, fechaUnix)
    }

    override fun onListSuccess(eventList: List<Event>) {
        listener?.onListSuccess(eventList)
    }

    override fun onNoData() {
        listener?.onNoData()
    }
}