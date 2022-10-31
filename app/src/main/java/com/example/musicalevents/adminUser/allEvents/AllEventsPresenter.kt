package com.example.musicalevents.adminUser.allEvents

import com.example.musicalevents.data.model.Event

class AllEventsPresenter(var view: AllEventsContract.View?) : AllEventsContract.Presenter {

    private var interactor: AllEventsInteractor? = null

    init {
        interactor = AllEventsInteractor(this)
    }

    override fun getAllEvents(year: String, month: String, day: String) {
        view?.showProgress()
        interactor?.getAllEvents(year, month, day)
    }

    override fun onDestroy() {
        view = null
        interactor = null
    }

    override fun onListSuccess(eventList: List<Event>) {
        view?.hideProgress()
        view?.onListSuccess(eventList)
    }

    override fun onNoData() {
        view!!.onNoData()
    }
}