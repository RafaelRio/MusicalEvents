package com.example.musicalevents.normalUser.allEvents

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicalevents.R
import com.example.musicalevents.adminUser.allEvents.AllEventsContract
import com.example.musicalevents.adminUser.allEvents.AllEventsPresenter
import com.example.musicalevents.data.model.Event
import com.example.musicalevents.databinding.FragmentAdminBinding
import com.example.musicalevents.databinding.FragmentUserBinding
import com.example.musicalevents.utils.EventoListAdapter
import com.example.musicalevents.utils.UserEventoListAdapter
import java.time.LocalDate

class UserFragment : Fragment() , UserEventoListAdapter.onManageEventoListener, UserAllEventsContract.View{

    private lateinit var binding : FragmentUserBinding
    private var adapter: UserEventoListAdapter? = null
    private lateinit var presenter : UserAllEventsContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = UserAllEventsPresenter(this)
    }

    override fun onStart() {
        super.onStart()
        val actualDate = LocalDate.now()
        presenter.getAllEvents((actualDate.year).toString(), (actualDate.monthValue).toString(), (actualDate.dayOfMonth).toString())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.calendarView.setOnDateChangeListener { _, year, month, day ->
            initRv()
            presenter.getAllEvents(year.toString(), (month + 1).toString(), day.toString())
        }

    }

    private fun initRv(){
        adapter = UserEventoListAdapter(ArrayList(), this)
        //2.- OBLIGATORIOMENTE se debe indicae que diseño (layout) tendra el recycler view
        val linearLayoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        //3.- Asgino el layout al recyclerView
        binding.rvEventos.layoutManager = linearLayoutManager
        //4.- Asigno a la vista sus datos (modelo)
        binding.rvEventos.adapter = adapter
    }

    override fun onListSuccess(eventList: List<Event>) {
        adapter?.update(eventList)
    }

    override fun onNoData() {
        Toast.makeText(context, "No hay datos", Toast.LENGTH_SHORT).show()
    }

    override fun onInfoEvent(l: Event) {
        val action = UserFragmentDirections.actionUserFragmentToUserEventInfoFragment(l)
        NavHostFragment.findNavController(this@UserFragment).navigate(action)
    }
}