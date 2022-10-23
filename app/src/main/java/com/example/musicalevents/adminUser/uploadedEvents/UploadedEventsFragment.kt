package com.example.musicalevents.adminUser.uploadedEvents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicalevents.data.model.Event
import com.example.musicalevents.databinding.FragmentUploadedEventsBinding
import com.example.musicalevents.utils.EventoCrudAdapter

class UploadedEventsFragment : Fragment(), UploadedEventsContract.View,
    EventoCrudAdapter.onManageEventoListener {

    private lateinit var binding: FragmentUploadedEventsBinding
    private lateinit var adapter: EventoCrudAdapter
    private lateinit var presenter: UploadedEventsContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = UploadedEventsPresenter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUploadedEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRv()
    }

    override fun onStart() {
        super.onStart()
        presenter.getMyEvents()
    }

    fun initRv() {
        adapter =
            EventoCrudAdapter(ArrayList(), this)
        //2.- OBLIGATORIOMENTE se debe indicae que diseño (layout) tendra el recycler view
        val linearLayoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        //3.- Asgino el layout al recyclerView
        binding.rvMyEvents.layoutManager = linearLayoutManager
        //4.- Asigno a la vista sus datos (modelo)
        binding.rvMyEvents.adapter = adapter
    }

    override fun showProgress() {

    }

    override fun hideProgress() {

    }

    override fun onListSuccess(eventos: List<Event>) {
        binding.imvNodata.visibility = View.GONE
        adapter.update(eventos)
    }

    override fun onDeleteSuccess(deletedEvent: Event) {
        adapter.removeItem(deletedEvent)
        Toast.makeText(context, "Evento borrado con exito", Toast.LENGTH_SHORT).show()
        if(adapter.itemCount == 0){
            onNoData()
        }
    }

    override fun onNoData() {
        binding.imvNodata.visibility = View.VISIBLE
        binding.rvMyEvents.visibility = View.GONE
    }

    override fun onEditEvento(l: Event?) {

    }

    override fun onDeleteEvento(l: Event) {
        presenter.delete(l)
    }
}