package com.example.musicalevents.normalUser.allEvents

import com.example.musicalevents.data.model.Event
import com.example.musicalevents.data.repository.JavaEventRepository

class UserAllEventsInteractor(private var listener: UserAllEventsContract.OnRepositoryCallback?) : UserAllEventsContract.Interactor {

    override fun getAllEvents(year: String, month: String, day: String) {
        JavaEventRepository.getInstance().userGetAllEvents(this, year, month, day)
    }

    override fun onListSuccess(eventList: List<Event>) {
        listener?.onListSuccess(eventList)
    }

    override fun onNoData() {
        listener?.onNoData()
    }
}