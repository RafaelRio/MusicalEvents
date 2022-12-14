package com.example.musicalevents.adminUser.uploadedEvents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicalevents.R
import com.example.musicalevents.data.model.Event
import com.example.musicalevents.databinding.FragmentUploadedEventsBinding
import com.example.musicalevents.mvp.uploadedevents.UploadedEventsContract
import com.example.musicalevents.mvp.uploadedevents.UploadedEventsPresenter
import com.example.musicalevents.utils.EventoCrudAdapterKt

/**
 * Fragment que carga la vista con una lista de eventos subidos por el usuario logeado
 * @author Rafa
 */

class UploadedEventsFragment : Fragment(), UploadedEventsContract.View,
    EventoCrudAdapterKt.OnManageEventoListener {

    private lateinit var binding: FragmentUploadedEventsBinding
    private lateinit var adapter: EventoCrudAdapterKt
    private lateinit var presenter: UploadedEventsContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = UploadedEventsPresenter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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

    private fun initRv() {
        adapter =
            EventoCrudAdapterKt(ArrayList(), this)
        //2.- OBLIGATORIOMENTE se debe indicae que dise??o (layout) tendra el recycler view
        val linearLayoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        //3.- Asgino el layout al recyclerView
        binding.rvMyEvents.layoutManager = linearLayoutManager
        //4.- Asigno a la vista sus datos (modelo)
        binding.rvMyEvents.adapter = adapter
    }

    override fun onListSuccess(eventList: List<Event>) {
        binding.imvNodata.visibility = View.GONE
        adapter.update(eventList)
    }

    override fun onDeleteSuccess(deletedEvent: Event) {
        adapter.removeItem(deletedEvent)
        Toast.makeText(context, R.string.delete_succeded, Toast.LENGTH_SHORT).show()
        if(adapter.itemCount == 0){
            onNoData()
        }
    }

    override fun onEditSuccess(editedEvent: Event) {
        Toast.makeText(context, R.string.edited_event, Toast.LENGTH_SHORT).show()
    }

    override fun onNoData() {
        binding.imvNodata.visibility = View.VISIBLE
        binding.rvMyEvents.visibility = View.GONE
    }

    override fun onEditEvento(l: Event) {
        val action = UploadedEventsFragmentDirections.actionUploadedEventsFragmentToEditEventFragment(l)
        NavHostFragment.findNavController(this@UploadedEventsFragment).navigate(action)
    }

    override fun onDeleteEvento(l: Event) {
        presenter.delete(l)
    }
}