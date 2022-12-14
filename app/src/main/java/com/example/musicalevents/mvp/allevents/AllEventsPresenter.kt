package com.example.musicalevents.mvp.allevents

import com.example.musicalevents.data.model.Event

/**
 * Presenter que comunica interactor y vista de todos los eventos subidos
 * @author Rafa
 */

class AllEventsPresenter(var view: AllEventsContract.View?) : AllEventsContract.Presenter {

    private var interactor: AllEventsInteractor? = null

    init {
        interactor = AllEventsInteractor(this)
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