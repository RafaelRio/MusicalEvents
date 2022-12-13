package com.example.musicalevents.normalUser.allEvents

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.appcompat.view.menu.MenuBuilder
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicalevents.R
import com.example.musicalevents.data.model.Event
import com.example.musicalevents.data.model.Userkt
import com.example.musicalevents.data.repository.LoginRepository
import com.example.musicalevents.databinding.FragmentUserBinding
import com.example.musicalevents.login.LoginActivitykt
import com.example.musicalevents.mvp.allevents.AllEventsContract
import com.example.musicalevents.mvp.allevents.AllEventsPresenter
import com.example.musicalevents.utils.EventoListAdapterKt
import java.text.SimpleDateFormat

class UserFragment : Fragment(), EventoListAdapterKt.OnManageEventoListener,
    AllEventsContract.View {
    private lateinit var binding: FragmentUserBinding
    private var adapter: EventoListAdapterKt? = null
    private lateinit var currentUser: Userkt
    private lateinit var prefs: SharedPreferences
    private lateinit var presenter: AllEventsContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = AllEventsPresenter(this)
        prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val name = prefs.getString("name", "")
        val admin = prefs.getBoolean("admin", false)
        val email = prefs.getString("email", "")
        val password = prefs.getString("password", "")
        val instagram = prefs.getString("instagram", "")
        val twitter = prefs.getString("twitter", "")
        val facebook = prefs.getString("facebook", "")
        val website = prefs.getString("website", "")
        currentUser = Userkt(
            name = name,
            email = email,
            isAdmin = admin,
            password = password,
            instagram = instagram,
            twitter = twitter,
            facebook = facebook,
            website = website
        )
        LoginRepository.currentUser = currentUser
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        menuCreation()
        presenter.getAllEvents(System.currentTimeMillis())
        binding.tvWelcome.text =
            "${getString(R.string.welcome)}, ${currentUser.name?.replaceFirstChar { currentUser.name!![0].uppercaseChar() }
                ?.trim()}"

        binding.calendarView.setOnDateChangeListener { _, year, month, day ->
            initRv()
            val fechaInicio = SimpleDateFormat("d/M/y").parse("$day/${month + 1}/$year")

            if (fechaInicio != null) {
                presenter.getAllEvents(fechaInicio.time)
            }
        }

    }

    private fun initRv() {
        presenter.getAllEvents(System.currentTimeMillis())
        adapter = EventoListAdapterKt(ArrayList(), this)
        val linearLayoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        binding.rvEventos.layoutManager = linearLayoutManager
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

    private fun menuCreation() {
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                if (menu is MenuBuilder) {
                    menu.setOptionalIconsVisible(true)
                }
                menuInflater.inflate(R.menu.user_menu, menu)

            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.about_us -> {
                        NavHostFragment.findNavController(this@UserFragment)
                            .navigate(R.id.action_userFragment_to_aboutUsFragment2)
                        return true
                    }

                    R.id.profile -> {
                        val action = UserFragmentDirections.actionUserFragmentToUserProfileFragment(currentUser)
                        NavHostFragment.findNavController(this@UserFragment).navigate(action)
                        return true
                    }

                    R.id.close_session -> {
                        prefs.edit().clear().apply()
                        startActivity(Intent(context, LoginActivitykt::class.java))
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
}