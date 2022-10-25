package com.example.musicalevents.adminUser.uploadedEvents

import com.example.musicalevents.data.model.Event

class UploadedEventsPresenter (var view: UploadedEventsContract.View?) : UploadedEventsContract.Presenter {

    private var interactor: UploadedEventsInteractor? = null

    init {
        interactor = UploadedEventsInteractor(this)
    }
    override fun delete(event: Event) {
        interactor?.delete(event)
    }

    override fun getMyEvents() {
        view?.showProgress()
        interactor?.getMyEvents()
    }

    override fun onDestroy() {
        view = null
        interactor = null
    }

    override fun onListSuccess(eventList: List<Event>) {
        view?.hideProgress()
        view?.onListSuccess(eventList)
    }

    override fun onDeleteSuccess(deletedEvent: Event) {
        view?.onDeleteSuccess(deletedEvent)
    }

    override fun onEditSuccess(editedEvent: Event) {
        view?.onEditSuccess(editedEvent)
    }

    override fun onNoData() {
        view?.onNoData()
    }
}