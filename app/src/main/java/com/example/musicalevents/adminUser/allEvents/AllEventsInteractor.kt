package com.example.musicalevents.adminUser.allEvents

import com.example.musicalevents.data.model.Event
import com.example.musicalevents.data.repository.JavaEventRepository

class AllEventsInteractor(private var listener: AllEventsContract.OnRepositoryCallback?) : AllEventsContract.Interactor {

    override fun getAllEvents(fechaUnix: Long) {
        JavaEventRepository.getInstance().getAllEvents(this, fechaUnix)
    }

    override fun onListSuccess(eventList: List<Event>) {
        listener?.onListSuccess(eventList)
    }

    override fun onNoData() {
        listener?.onNoData()
    }
}