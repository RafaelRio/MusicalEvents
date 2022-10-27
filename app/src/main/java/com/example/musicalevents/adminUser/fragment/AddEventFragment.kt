package com.example.musicalevents.adminUser.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.musicalevents.R
import com.example.musicalevents.data.model.Event
import com.example.musicalevents.data.repository.JavaEventRepository
import com.example.musicalevents.databinding.FragmentAddEventBinding
import com.example.musicalevents.utils.DatePickerFragment
import com.example.musicalevents.utils.TimePickerFragment
import java.util.*

class AddEventFragment : Fragment() {

    private lateinit var binding : FragmentAddEventBinding
    private lateinit var eventRepository : JavaEventRepository
    val startCalendar = Calendar.getInstance()
    val endCalendar = Calendar.getInstance()
    var e = Event()
    var diaComienzo: Int = 0
    var mesComienzo: Int = 0
    var anioComienzo: Int = 0
    var diaFin: Int = 0
    var mesFin: Int = 0
    var anioFin: Int = 0
    var errorCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        eventRepository = JavaEventRepository()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAddEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tieFecha.setOnClickListener {
            showDatePickerDialog()
        }
        binding.tieEndFecha.setOnClickListener {
            showDatePickerDialog2()
        }

        binding.tieHoraComienzoEvento.setOnClickListener {
            showTimePickerDialog()
        }
        binding.tieHoraFinEvento.setOnClickListener {
            showTimePickerDialog2()
        }

        binding.fabAddEvento.setOnClickListener{
            if (validateFields() >= 1){
                return@setOnClickListener
            }

            e.nombreEvento = binding.tieNombreEvento.text.toString()
            e.ubicacion = binding.tieUbicacionEvento.text.toString()
            e.descripcion = binding.tieDescripcionEvento.text.toString()
            e.diaComienzo = diaComienzo
            e.mesComienzo = mesComienzo
            e.anioComienzo = anioComienzo
            e.horaComienzo = binding.tieHoraComienzoEvento.text.toString()
            e.diaFin = diaFin
            e.mesFin = mesFin
            e.anioFin = anioFin
            e.horaFin = binding.tieHoraFinEvento.text.toString()

            eventRepository.uploadEvent(e)
            Handler(Looper.getMainLooper()).postDelayed({
                //Esto simplemente espera un momento para volver atrás
                Toast.makeText(context, getString(R.string.success_uploading_event), Toast.LENGTH_LONG).show()
                findNavController().navigateUp()
            }, 1500)
        }
    }

    private fun validateFields() : Int{
        errorCount = 0

        val startDate: Date = startCalendar.time
        val endDate: Date = endCalendar.time




        if(binding.tieNombreEvento.text?.isBlank() == true){
            Toast.makeText(context, R.string.empty_eventName, Toast.LENGTH_SHORT).show()
            errorCount+=1
            return errorCount
        }

        if(binding.tieUbicacionEvento.text?.isBlank() == true){
            Toast.makeText(context, R.string.empty_eventLocation, Toast.LENGTH_SHORT).show()
            errorCount+=1
            return errorCount
        }

        if(binding.tieFecha.text?.isBlank() == true){
            Toast.makeText(context, R.string.empty_eventDate, Toast.LENGTH_SHORT).show()
            errorCount+=1
            return errorCount
        }

        if(binding.tieEndFecha.text?.isBlank() == true){
            Toast.makeText(context, R.string.empty_eventDate, Toast.LENGTH_SHORT).show()
            errorCount+=1
            return errorCount
        }

        if(binding.tieHoraComienzoEvento.text?.isBlank() == true){
            Toast.makeText(context, R.string.empty_eventHour, Toast.LENGTH_SHORT).show()
            errorCount+=1
            return errorCount
        }

        if(binding.tieHoraFinEvento.text?.isBlank() == true){
            Toast.makeText(context, R.string.empty_eventHour, Toast.LENGTH_SHORT).show()
            errorCount+=1
            return errorCount
        }

        if (!endDate.after(startDate)){
            Toast.makeText(context, R.string.error_date, Toast.LENGTH_SHORT).show()
            errorCount+=1
            return errorCount
        }

        if(binding.tieDescripcionEvento.text?.isBlank() == true){
            Toast.makeText(context, R.string.empty_eventDescription, Toast.LENGTH_SHORT).show()
            errorCount+=1
            return errorCount
        }

        return errorCount
    }

    private fun showDatePickerDialog() {
        val newFragment: DatePickerFragment =
            DatePickerFragment.newInstance { _, year, month, day -> // +1 because January is zero
                val monthFormatted = String.format("%02d", month + 1)
                val dayOfMonthFormatted = String.format("%02d", day)
                binding.tieFecha.setText("$dayOfMonthFormatted/$monthFormatted/$year")

                val fecha: Array<String> = binding.tieFecha.text.toString().split("/").toTypedArray()
                diaComienzo = Integer.parseInt(fecha[0])
                mesComienzo = Integer.parseInt(fecha[1])
                anioComienzo = Integer.parseInt(fecha[2])

                startCalendar.set(Calendar.YEAR, anioComienzo)
                startCalendar.set(Calendar.MONTH, mesComienzo)
                startCalendar.set(Calendar.DAY_OF_MONTH, diaComienzo)
            }
        newFragment.show(requireActivity().supportFragmentManager, "datePicker")
    }

    private fun showDatePickerDialog2() {
        val newFragment: DatePickerFragment =
            DatePickerFragment.newInstance { _, year, month, day -> // +1 because January is zero
                val monthFormatted = String.format("%02d", month + 1)
                val dayOfMonthFormatted = String.format("%02d", day)
                binding.tieEndFecha.setText("$dayOfMonthFormatted/$monthFormatted/$year")

                val fecha: Array<String> = binding.tieEndFecha.text.toString().split("/").toTypedArray()
                diaFin = Integer.parseInt(fecha[0])
                mesFin = Integer.parseInt(fecha[1])
                anioFin = Integer.parseInt(fecha[2])

                endCalendar.set(Calendar.YEAR, anioFin)
                endCalendar.set(Calendar.MONTH, mesFin)
                endCalendar.set(Calendar.DAY_OF_MONTH, diaFin)
            }
        newFragment.show(requireActivity().supportFragmentManager, "datePicker")
    }

    private fun showTimePickerDialog() {
        val newFragment = TimePickerFragment{
            onTimeSelected(it)
            startCalendar.set(Calendar.HOUR, Integer.parseInt(it.split(":")[0]))
            startCalendar.set(Calendar.MINUTE, Integer.parseInt(it.split(":")[1]))
        }
        newFragment.show(requireActivity().supportFragmentManager, "timepicker")
    }

    private fun onTimeSelected(time: String) {
        binding.tieHoraComienzoEvento.setText(time)
    }

    private fun showTimePickerDialog2() {
        val newFragment = TimePickerFragment{
            onTimeSelected2(it)
            endCalendar.set(Calendar.HOUR, Integer.parseInt(it.split(":")[0]))
            endCalendar.set(Calendar.MINUTE, Integer.parseInt(it.split(":")[1]))
        }
        newFragment.show(requireActivity().supportFragmentManager, "timepicker")
    }

    private fun onTimeSelected2(time: String) {
        binding.tieHoraFinEvento.setText(time)
    }

}