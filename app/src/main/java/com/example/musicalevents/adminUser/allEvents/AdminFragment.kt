package com.example.musicalevents.adminUser.allEvents

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicalevents.R
import com.example.musicalevents.adminUser.AdminActivity
import com.example.musicalevents.data.model.Event
import com.example.musicalevents.data.model.Userkt
import com.example.musicalevents.data.repository.LoginRepository
import com.example.musicalevents.databinding.FragmentAdminBinding
import com.example.musicalevents.login.LoginActivity
import com.example.musicalevents.utils.EventoListAdapter
import java.text.SimpleDateFormat
import java.time.LocalDate

class AdminFragment : Fragment(), EventoListAdapter.onManageEventoListener, AllEventsContract.View{

    private lateinit var binding : FragmentAdminBinding
    private var adapter: EventoListAdapter? = null
    private lateinit var presenter : AllEventsContract.Presenter
    private val currentUser : Userkt = LoginRepository.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = AllEventsPresenter(this)
    }

    override fun onStart() {
        super.onStart()
        val actualDate = LocalDate.now()
        val anio = String.format("%04d", actualDate.year)
        val mes = String.format("%02d", actualDate.monthValue)
        val dia = String.format("%02d", actualDate.dayOfMonth)
        presenter.getAllEvents(System.currentTimeMillis())
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
            val fechaInicio = SimpleDateFormat("d/M/y").parse("$day/${month + 1}/$year")

            presenter.getAllEvents(fechaInicio.time)
        }
    }

    private fun initRv(){
        adapter = EventoListAdapter(ArrayList(), this)
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

                    R.id.group_info -> {
                        val action = AdminFragmentDirections.actionAdminFragmentToGroupInformationFillFragment(currentUser)
                        NavHostFragment.findNavController(this@AdminFragment).navigate(action)
                        return true
                    }

                    R.id.close_session -> {
                        startActivity(Intent(context, LoginActivity::class.java))
                        requireActivity().finish()
                        return true
                    }
                    else -> {
                        false
                    }
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onInfoEvent(l: Event) {
        val action = AdminFragmentDirections.actionAdminFragmentToInfoEventFragment(l)
        NavHostFragment.findNavController(this@AdminFragment).navigate(action)
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

    override fun onDetach() {
        super.onDetach()
        requireActivity().finish()
    }
}