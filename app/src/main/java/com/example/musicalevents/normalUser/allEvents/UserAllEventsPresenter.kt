package com.example.musicalevents.normalUser.allEvents

import com.example.musicalevents.data.model.Event

class UserAllEventsPresenter(var view: UserAllEventsContract.View?) : UserAllEventsContract.Presenter {

    private var interactor: UserAllEventsInteractor? = null

    init {
        interactor = UserAllEventsInteractor(this)
    }

    override fun getAllEvents(fechaUnix: Long) {
        interactor?.getAllEvents(fechaUnix)
    }

    override fun onDestroy() {
        view = null
        interactor = null
    }

    override fun onListSuccess(eventList: List<Event>) {
        view?.onListSuccess(eventList)
    }

    override fun onNoData() {
        view!!.onNoData()
    }
}