package com.example.musicalevents.adminUser.allEvents

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicalevents.R
import com.example.musicalevents.adminUser.allEvents.AllEventsContract
import com.example.musicalevents.adminUser.allEvents.AllEventsPresenter
import com.example.musicalevents.data.model.Event
import com.example.musicalevents.databinding.FragmentAdminBinding
import com.example.musicalevents.utils.EventoAdapter
import java.time.LocalDate

class AdminFragment : Fragment(), EventoAdapter.onManageEventoListener, AllEventsContract.View{

    private lateinit var binding : FragmentAdminBinding
    private var adapter: EventoAdapter? = null
    private lateinit var presenter : AllEventsContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = AllEventsPresenter(this)
    }

    override fun onStart() {
        super.onStart()
        var actualDate = LocalDate.now()
        presenter.getAllEvents(actualDate.year, actualDate.monthValue, actualDate.dayOfMonth)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        menuCreation()

        binding.calendarView.setOnDateChangeListener { _, year, month, day ->

            initRv()

            presenter.getAllEvents(year, (month + 1), day)
        }

    }

    fun initRv(){
        adapter = EventoAdapter(ArrayList(), this)
        //2.- OBLIGATORIOMENTE se debe indicae que diseño (layout) tendra el recycler view
        val linearLayoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        //3.- Asgino el layout al recyclerView
        binding.rvEventos.layoutManager = linearLayoutManager
        //4.- Asigno a la vista sus datos (modelo)
        binding.rvEventos.adapter = adapter
    }

    private fun menuCreation(){
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.admin_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.add_event -> {
                        NavHostFragment.findNavController(this@AdminFragment)
                            .navigate(R.id.action_adminFragment_to_addEventFragment)
                        return true
                    }

                    R.id.my_events -> {
                        NavHostFragment.findNavController(this@AdminFragment)
                            .navigate(R.id.action_adminFragment_to_uploadedEventsFragment)
                        return true
                    }
                    else -> {
                        false
                    }
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onEditEvento(l: Event?) {

    }

    override fun onDeleteEvento(l: Event?) {

    }

    override fun showProgress() {
    }

    override fun hideProgress() {
    }

    override fun onListSuccess(eventList: List<Event>) {
        adapter?.update(eventList)
    }

    override fun onNoData() {
        Toast.makeText(context, "No hay datos", Toast.LENGTH_SHORT).show()
    }
}