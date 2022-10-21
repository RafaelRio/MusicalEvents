package com.example.musicalevents.adminUser.allEvents

import com.example.musicalevents.data.model.Event
import com.example.musicalevents.data.repository.JavaEventRepository

class AllEventsInteractor(private var listener: AllEventsContract.OnRepositoryCallback?) : AllEventsContract.Interactor {

    override fun getAllEvents(year: Int, month: Int, day: Int) {
        JavaEventRepository.getInstance().getAllEvents(this, year, month, day)
    }

    override fun onListSuccess(eventList: List<Event>) {
        listener?.onListSuccess(eventList)
    }

    override fun onNoData() {
        listener?.onNoData()
    }
}