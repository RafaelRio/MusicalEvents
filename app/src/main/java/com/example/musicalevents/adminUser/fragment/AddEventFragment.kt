package com.example.musicalevents.adminUser.fragment

import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.musicalevents.R
import com.example.musicalevents.data.model.Event
import com.example.musicalevents.data.model.Userkt
import com.example.musicalevents.data.repository.EventRepository
import com.example.musicalevents.data.repository.LoginRepository
import com.example.musicalevents.databinding.FragmentAddEventBinding
import com.example.musicalevents.utils.DatePickerFragment
import com.example.musicalevents.utils.TimePickerFragment
import java.util.*

class AddEventFragment : Fragment() {

    private lateinit var binding : FragmentAddEventBinding
    private lateinit var eventRepository : EventRepository
    companion object{
        var currentUser: Userkt = LoginRepository.currentUser
    }
    var dia: String? = null
    var mes: String? = null
    var anio: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tieFecha.setOnClickListener {
            showDatePickerDialog()
        }

        binding.tieHoraComienzoEvento.setOnClickListener {
            showTimePickerDialog()
        }

        binding.fabAddEvento.setOnClickListener{
//            currentUser = LoginRepository.currentUser
//            Toast.makeText(context, currentUser.email.toString(), Toast.LENGTH_LONG).show()

            var e = Event()
            e.nombreEvento = binding.tieNombreEvento.text.toString()
            e.ubicacion = binding.tieUbicacionEvento.text.toString()
            e.descripcion = binding.tieDescripcionEvento.text.toString()
            e.dia = dia
            e.mes = mes
            e.anio = anio
            eventRepository = EventRepository().getInstance()
            eventRepository.uploadEvent(e)
            Toast.makeText(context, getString(R.string.success_uploading_event), Toast.LENGTH_LONG).show()
        }
    }

    private fun showDatePickerDialog() {
        val newFragment: DatePickerFragment =
            DatePickerFragment.newInstance { datePicker, year, month, day -> // +1 because January is zero
                var selectedDate = day.toString() + "/" + (month + 1) + "/" + year
                if (day < 10 && month + 1 >= 10) {
                    selectedDate = "0" + day + "/" + (month + 1) + "/" + year
                }
                if (day >= 10 && month + 1 < 10) {
                    selectedDate = day.toString() + "/" + "0" + (month + 1) + "/" + year
                }
                if (day < 10 && month + 1 < 10) {
                    selectedDate = "0" + day + "/" + "0" + (month + 1) + "/" + year
                }
                binding.tieFecha.setText(selectedDate)
                val fecha: Array<String> =
                    binding.tieFecha.text.toString().split("/").toTypedArray()
                dia = fecha[0]
                mes = fecha[1]
                anio = fecha[2]
            }
        newFragment.show(requireActivity().supportFragmentManager, "datePicker")
    }

    private fun showTimePickerDialog() {
        val newFragment: TimePickerFragment = TimePickerFragment{
            onTimeSelected(it)
        }
        newFragment.show(requireActivity().supportFragmentManager, "timepicker")
    }

    private fun onTimeSelected(time: String) {
        binding.tieHoraComienzoEvento.setText("Reserva para las $time")
    }

}