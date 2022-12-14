package com.example.musicalevents.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.location.Address
import android.location.Geocoder
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.example.musicalevents.R
import com.example.musicalevents.data.model.Event
import com.example.musicalevents.data.model.Userkt
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

/**
 * Clase de utilidades, constantes de firebase
 * @author Rafa
 */
object UtilsKt {

    //Constantes de firebase
    const val eventosTable = "eventos"
    const val personasTable = "personas"
    var latitud = 0.0
    var longitud = 0.0

    fun setDateHour(text: TextView, hour: TextView, calendar: Calendar) {
        val dayOfMonth = String.format("%02d", calendar[Calendar.DAY_OF_MONTH])
        val month = String.format("%02d", calendar[Calendar.MONTH] + 1)
        val year = String.format("%04d", calendar[Calendar.YEAR])
        val hourOfDay = String.format("%02d", calendar[Calendar.HOUR_OF_DAY])
        val minute = String.format("%02d", calendar[Calendar.MINUTE])

        text.text = "$dayOfMonth/$month/$year"
        hour.text = "$hourOfDay:$minute"
    }

    fun onBindViewHolder(
        holder: EventoListAdapterKt.ViewHolder,
        position: Int,
        eventos: MutableList<Event>,
        listener: EventoListAdapterKt.OnManageEventoListener
    ) {
        holder.ubicacion.text = eventos[position].location
        holder.nombre.text = eventos[position].eventName
        val eventDate = Calendar.getInstance()
        eventDate.timeInMillis = eventos[position].startDate
        val anio = String.format("%02d", eventDate[Calendar.YEAR])
        val mes = String.format("%02d", (eventDate[Calendar.MONTH] + 1))
        val dia = String.format("%02d", eventDate[Calendar.DAY_OF_MONTH])
        val hora = String.format("%02d", eventDate[Calendar.HOUR_OF_DAY])
        val minutos = String.format("%02d", eventDate[Calendar.MINUTE])
        val animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.slide_in)
        eventos.sortBy { it.eventName }
        holder.itemView.startAnimation(animation)
        holder.hora.text = "$dia/$mes/$anio - $hora:$minutos"
        holder.bind(eventos[position], listener)
    }

    fun getAddress(lat: Double, lon: Double, context: Context): String? {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses: List<Address> =
            lat.let { it1 -> geocoder.getFromLocation(it1, lon, 10) } as List<Address>
        if (addresses[0].thoroughfare == null) {
            Toast.makeText(context, R.string.error_no_street, Toast.LENGTH_LONG).show()
            return null
        }
        if (addresses[0].locality == null) {
            Toast.makeText(context, R.string.error_no_locality, Toast.LENGTH_LONG).show()
            return null
        }
        return "${addresses[0].thoroughfare}, ${addresses[0].locality}, ${addresses[0].countryName}"
    }

    fun onBindViewHolder(
        holder: EventoCrudAdapterKt.ViewHolder,
        position: Int,
        eventos: MutableList<Event>,
        listener: EventoCrudAdapterKt.OnManageEventoListener
    ) {
        holder.ubicacion.text = eventos[position].location
        holder.nombre.text = eventos[position].eventName
        val eventDate = Calendar.getInstance()
        eventDate.timeInMillis = eventos[position].startDate
        val anio = String.format("%02d", eventDate[Calendar.YEAR])
        val mes = String.format("%02d", (eventDate[Calendar.MONTH] + 1))
        val dia = String.format("%02d", eventDate[Calendar.DAY_OF_MONTH])
        val hora = String.format("%02d", eventDate[Calendar.HOUR_OF_DAY])
        val minutos = String.format("%02d", eventDate[Calendar.MINUTE])
        val animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.slide_in)
        eventos.sortBy { it.eventName }
        holder.itemView.startAnimation(animation)
        holder.hora.text = "$dia/$mes/$anio - $hora:$minutos"
        holder.bind(eventos[position], listener)
    }

    private fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm")
        return format.format(date)
    }

    fun disableDarkMode(activity: Activity) {
        when (activity.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> {
                AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO
                );
            }
        }
    }

    fun update(eventos: MutableList<Event>, events: List<Event>?) {
        eventos.clear()
        eventos.addAll(events!!)
    }

    fun isPasswordValid(password: String): Boolean {
        val passwordPattern =
            "(?=.*[0-9])" +                         //at least 1 digit
                    "(?=.*[a-z])" +                         //at least 1 lower case letter
                    "(?=.*[A-Z])" +                         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +                      //any letter
                    "(?=\\S+$)" +                           //no white spaces
                    ".{8,}" +                               //at least 8 characters
                    "$"
        val pattern = Pattern.compile(passwordPattern);
        val matcher = pattern.matcher(password);
        return matcher.matches();
    }

    fun shareEvent(eventCalendar: Event, activity: Activity) {
        val dia = convertLongToTime(eventCalendar.startDate)
        val texto =
            "${activity.getString(R.string.come_with_me)} ${eventCalendar.eventName} ${
                activity.getString(R.string._in)
            } ${eventCalendar.location} ${activity.getString(R.string.at)} $dia"
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, texto)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        activity.startActivity(shareIntent)
    }
}