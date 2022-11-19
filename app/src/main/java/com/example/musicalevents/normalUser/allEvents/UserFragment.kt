package com.example.musicalevents.normalUser.allEvents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicalevents.data.model.Event
import com.example.musicalevents.databinding.FragmentUserBinding
import com.example.musicalevents.utils.EventoListAdapterKt
import java.text.SimpleDateFormat
import java.time.LocalDate

class UserFragment : Fragment() , EventoListAdapterKt.OnManageEventoListener, UserAllEventsContract.View{

    private lateinit var binding : FragmentUserBinding
    private var adapter: EventoListAdapterKt? = null
    private lateinit var presenter : UserAllEventsContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = UserAllEventsPresenter(this)
    }

    override fun onStart() {
        super.onStart()
        presenter.getAllEvents(System.currentTimeMillis())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.calendarView.setOnDateChangeListener { _, year, month, day ->
            initRv()
            val fechaInicio = SimpleDateFormat("d/M/y").parse("$day/${month + 1}/$year")

            presenter.getAllEvents(fechaInicio.time)
        }

    }

    private fun initRv(){
        adapter = EventoListAdapterKt(ArrayList(), this)
        //2.- OBLIGATORIOMENTE se debe indicae que diseño (layout) tendra el recycler view
        val linearLayoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        //3.- Asgino el layout al recyclerView
        binding.rvEventos.layoutManager = linearLayoutManager
        //4.- Asigno a la vista sus datos (modelo)
        binding.rvEventos.adapter = adapter
    }

    override fun onListSuccess(eventList: List<Event>) {
        binding.imvNodata.visibility = View.GONE
        binding.rvEventos.visibility = View.VISIBLE
        adapter?.update(eventList)
    }

    override fun onNoData() {
        binding.imvNodata.visibility = View.VISIBLE
        binding.rvEventos.visibility = View.GONE
    }

    override fun onInfoEvent(l: Event) {
        val action = UserFragmentDirections.actionUserFragmentToUserEventInfoFragment(l)
        NavHostFragment.findNavController(this@UserFragment).navigate(action)
    }
}