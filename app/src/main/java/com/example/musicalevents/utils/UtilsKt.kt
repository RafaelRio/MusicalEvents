package com.example.musicalevents.utils

import android.app.Activity
import android.content.SharedPreferences
import android.content.res.Configuration
import android.preference.PreferenceManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import com.example.musicalevents.data.model.Event
import com.example.musicalevents.data.model.Userkt
import com.example.musicalevents.data.repository.LoginRepository
import com.google.android.material.textfield.TextInputEditText
import java.util.*
import java.util.regex.Pattern

class UtilsKt {


    companion object {
        //Constantes de firebase
        val eventosTable = "eventos"
        val personasTable = "personas"


        fun setDateHour(text: TextInputEditText, hour: TextInputEditText, calendar: Calendar) {
            val dayOfMonth = String.format("%02d", calendar[Calendar.DAY_OF_MONTH])
            val month = String.format("%02d", calendar[Calendar.MONTH] + 1)
            val year = String.format("%04d", calendar[Calendar.YEAR])
            val hourOfDay = String.format("%02d", calendar[Calendar.HOUR_OF_DAY])
            val minute = String.format("%02d", calendar[Calendar.MINUTE])

            text.setText("$dayOfMonth/$month/$year")
            hour.setText("$hourOfDay:$minute")
        }

        fun setDateHour(text: TextView, hour: TextView, calendar: Calendar) {
            val dayOfMonth = String.format("%02d", calendar[Calendar.DAY_OF_MONTH])
            val month = String.format("%02d", calendar[Calendar.MONTH] + 1)
            val year = String.format("%04d", calendar[Calendar.YEAR])
            val hourOfDay = String.format("%02d", calendar[Calendar.HOUR_OF_DAY])
            val minute = String.format("%02d", calendar[Calendar.MINUTE])

            text.text = "$dayOfMonth/$month/$year"
            hour.text = "$hourOfDay:$minute"
        }

        fun shareEvent(
            eventCalendar: Event
        ): String {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = eventCalendar.fechaInicioMiliSegundos
            val dayOfMonth = String.format("%02d", calendar[Calendar.DAY_OF_MONTH])
            val month = String.format("%02d", calendar[Calendar.MONTH] + 1)
            val year = String.format("%04d", calendar[Calendar.YEAR])
            val hourOfDay = String.format("%02d", calendar[Calendar.HOUR_OF_DAY])
            val minute = String.format("%02d", calendar[Calendar.MINUTE])

            val fecha = "$dayOfMonth/$month/$year"
            val hora = "$hourOfDay:$minute"
            return "Ven a ver ${eventCalendar.nombreEvento} conmigo el $fecha a las $hora en ${eventCalendar.ubicacion}"
        }

        fun calendarSetDate(
            calendar: Calendar,
            anio: String,
            mes: String,
            dia: String
        ) {
            calendar.set(Calendar.YEAR, anio.toInt())
            calendar.set(Calendar.MONTH, mes.toInt() - 1)
            calendar.set(Calendar.DAY_OF_MONTH, dia.toInt())
        }

        fun calendarSetHour(
            calendar: Calendar,
            horaMinuto: String
        ) {
            calendar.set(Calendar.HOUR, Integer.parseInt(horaMinuto.split(":")[0]))
            calendar.set(Calendar.MINUTE, Integer.parseInt(horaMinuto.split(":")[1]))
        }

        fun onBindViewHolder(holder: EventoListAdapterKt.ViewHolder, position: Int, eventos: MutableList<Event>, listener: EventoListAdapterKt.OnManageEventoListener) {
            holder.ubicacion.text = eventos[position].ubicacion
            holder.nombre.text = eventos[position].nombreEvento
            val eventDate = Calendar.getInstance()
            eventDate.timeInMillis = eventos[position].fechaInicioMiliSegundos
            val hora = String.format("%02d", eventDate[Calendar.HOUR_OF_DAY])
            val minutos = String.format("%02d", eventDate[Calendar.MINUTE])
            holder.hora.text = "$hora:$minutos"
            holder.bind(eventos[position], listener)
        }

        fun onBindViewHolder(holder: EventoCrudAdapterKt.ViewHolder, position: Int, eventos: MutableList<Event>, listener: EventoCrudAdapterKt.OnManageEventoListener) {
            holder.ubicacion.text = eventos[position].ubicacion
            holder.nombre.text = eventos[position].nombreEvento
            val eventDate = Calendar.getInstance()
            eventDate.timeInMillis = eventos[position].fechaInicioMiliSegundos
            val hora = String.format("%02d", eventDate[Calendar.HOUR_OF_DAY])
            val minutos = String.format("%02d", eventDate[Calendar.MINUTE])
            holder.hora.text = "$hora:$minutos"
            holder.bind(eventos[position], listener)
        }

        fun disableDarkMode(activity: Activity){
            val nightModeFlags: Int = activity.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            when (nightModeFlags) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        }

        fun update(eventos: MutableList<Event>, events: List<Event>?){
            eventos.clear()
            eventos.addAll(events!!)
        }

        @JvmStatic
        fun isPasswordValid(password: String): Boolean {
            val PASSWORDPATTERN = "(?=.*[0-9])" +           //at least 1 digit
                    "(?=.*[a-z])" +                         //at least 1 lower case letter
                    "(?=.*[A-Z])" +                         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +                      //any letter
                    "(?=\\S+$)" +                           //no white spaces
                    ".{8,}" +                               //at least 8 characters
                    "$"
            val pattern = Pattern.compile(PASSWORDPATTERN);
            val matcher = pattern.matcher(password);
            return matcher.matches();
        }
    }

}