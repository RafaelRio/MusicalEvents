package com.example.musicalevents.adminUser.eventInfo

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.CalendarContract
import android.view.*
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.navArgs
import com.example.musicalevents.R
import com.example.musicalevents.data.model.Event
import com.example.musicalevents.databinding.FragmentInfoEventBinding
import com.example.musicalevents.utils.UtilsKt
import java.util.*

class InfoEventFragment : Fragment() {

    private lateinit var binding: FragmentInfoEventBinding
    private val args: InfoEventFragmentArgs by navArgs()
    private lateinit var eventCalendar: Event

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        eventCalendar = args.calendarEvent
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInfoEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        menuCreation()
        bindingFields()
        hideButtons()

        binding.apply {
            instagramButton.setOnClickListener {
                eventCalendar.user.instagram?.let { it1 ->
                    openLinks("https://www.instagram.com/${it1}/", R.string.error_instagramnotfound)

                }
            }

            twitterButton.setOnClickListener {
                eventCalendar.user.twitter?.let { it1 ->
                    openLinks("https://twitter.com/$it1", R.string.error_twitternotfound)
                }
            }

            facebookButton.setOnClickListener {
                eventCalendar.user.facebook?.let { it1 ->
                    openLinks("https://www.facebook.com/$it1", R.string.error_facebooknotfound)
                }
            }

            websiteButton.setOnClickListener {
                eventCalendar.user.website?.let { it1 ->
                    openLinks(it1, R.string.error_websitenotfound)
                }
            }

            infoUbicacionEvento.setOnClickListener {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?&daddr=${eventCalendar.lat},${eventCalendar.lon}")
                )
                startActivity(intent)
            }
        }
    }

    private fun hideButtons() {
        if (eventCalendar.user.instagram?.isBlank() == true) {
            binding.instagramButton.visibility = View.GONE
        }
        if (eventCalendar.user.twitter?.isBlank() == true) {
            binding.twitterButton.visibility = View.GONE
        }
        if (eventCalendar.user.facebook?.isBlank() == true) {
            binding.facebookButton.visibility = View.GONE
        }
        if (eventCalendar.user.website?.isBlank() == true) {
            binding.websiteButton.visibility = View.GONE
        }
    }

    private fun openLinks(link: String, @StringRes error: Int) {
        if (link.isEmpty()) {
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            return
        }
        try {
            val uri = Uri.parse(link)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)

        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, R.string.error_badLink, Toast.LENGTH_SHORT).show()
        }

    }

    private fun bindingFields() {

        val calInicio = Calendar.getInstance()
        calInicio.timeInMillis = eventCalendar.startDate

        val calFin = Calendar.getInstance()
        calFin.timeInMillis = eventCalendar.endDate

        binding.apply {
            infoNombreEvento.text = eventCalendar.eventName
            infoUbicacionEvento.text = UtilsKt.getAddress(eventCalendar.lat, eventCalendar.lon, requireContext())
            infoDescripcionEvento.text = eventCalendar.description
            UtilsKt.setDateHour(infoInicioFechaEvento, infoHoraInicioEvento, calInicio)
            UtilsKt.setDateHour(infoFechaFinEvento, infoHoraFinEvento, calFin)
        }
    }

    private fun menuCreation() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.event_info_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.create_calendar_event -> {
                        createGoogleCalendarEvent()
                        return true
                    }
                    R.id.share_event -> {
                        UtilsKt.shareEvent(eventCalendar = eventCalendar, activity = requireActivity())

                        return true
                    }
                    else -> {
                        false
                    }
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    fun createGoogleCalendarEvent() {
        val startEvent = Calendar.getInstance()
        val endEvent = Calendar.getInstance()

        //Fecha inicio
        startEvent.set(Calendar.YEAR, binding.infoInicioFechaEvento.text.split("/")[2].toInt())
        startEvent.set(Calendar.MONTH, binding.infoInicioFechaEvento.text.split("/")[1].toInt() - 1)
        startEvent.set(
            Calendar.DAY_OF_MONTH,
            binding.infoInicioFechaEvento.text.split("/")[0].toInt()
        )

        //Hora inicio
        startEvent.set(
            Calendar.HOUR_OF_DAY,
            binding.infoHoraInicioEvento.text.split(":")[0].toInt()
        )
        startEvent.set(Calendar.MINUTE, binding.infoHoraInicioEvento.text.split(":")[1].toInt())

        //Fecha fin
        endEvent.set(Calendar.YEAR, binding.infoFechaFinEvento.text.split("/")[2].toInt())
        endEvent.set(Calendar.MONTH, binding.infoFechaFinEvento.text.split("/")[1].toInt() - 1)
        endEvent.set(Calendar.DAY_OF_MONTH, binding.infoFechaFinEvento.text.split("/")[0].toInt())

        //Hora fin
        endEvent.set(Calendar.HOUR_OF_DAY, binding.infoHoraFinEvento.text.split(":")[0].toInt())
        endEvent.set(Calendar.MINUTE, binding.infoHoraFinEvento.text.split(":")[1].toInt())


        val intent = Intent(Intent.ACTION_INSERT)
        intent.data = CalendarContract.Events.CONTENT_URI
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startEvent.timeInMillis)
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endEvent.timeInMillis)
        intent.putExtra(CalendarContract.Events.ALL_DAY, false)
        intent.putExtra(CalendarContract.Events.TITLE, binding.infoNombreEvento.text)
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, binding.infoUbicacionEvento.text)
        intent.putExtra(CalendarContract.Events.DESCRIPTION, binding.infoDescripcionEvento.text)

        if(intent.resolveActivity(requireActivity().packageManager) != null){
            startActivity(intent);
        }else{
            Toast.makeText(context, R.string.error_googlecalendar, Toast.LENGTH_SHORT).show();
        }
    }
}