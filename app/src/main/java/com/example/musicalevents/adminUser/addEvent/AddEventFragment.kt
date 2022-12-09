package com.example.musicalevents.adminUser.addEvent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.musicalevents.R
import com.example.musicalevents.data.model.Event
import com.example.musicalevents.data.repository.EventRepository
import com.example.musicalevents.databinding.FragmentAddEventBinding
import com.example.musicalevents.utils.DatePickerKt
import com.example.musicalevents.utils.TimePickerFragment
import com.example.musicalevents.utils.UtilsKt
import java.util.*

class AddEventFragment : Fragment() {

    private lateinit var binding: FragmentAddEventBinding
    private lateinit var eventRepository: EventRepository
    val calendar = Calendar.getInstance()
    var diaInicio = "0"
    var diaFin = "0"
    var mesInicio = "0"
    var mesFin = "0"
    var anioInicio = "0"
    var anioFin = "0"
    lateinit var startDate: Date
    lateinit var endDate: Date
    var newEvent = Event()

    var errorCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        eventRepository = EventRepository()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startDate = Date(newEvent.startDate)
        endDate = Date(newEvent.endDate)
        binding.apply {
            tieFecha.setOnClickListener {
                setStartDate()
            }
            tieEndFecha.setOnClickListener {
                setEndDate()
            }

            tieUbicacionEvento.setOnClickListener {
                findNavController().navigate(R.id.action_addEventFragment_to_mapFragment)
            }

            tieHoraComienzoEvento.setOnClickListener {
                setStartHour()
            }
            tieHoraFinEvento.setOnClickListener {
                setEndHour()
            }

            fabAddEvento.setOnClickListener {
                if (validateFields() >= 1) {
                    return@setOnClickListener
                }

                newEvent = Event(
                    eventName = tieNombreEvento.text.toString(),
                    location = tieUbicacionEvento.text.toString(),
                    lat = UtilsKt.latitud,
                    lon = UtilsKt.longitud,
                    description = tieDescripcionEvento.text.toString(),
                    startDate = startDate.time,
                    endDate = endDate.time,
                )

                eventRepository.uploadEvent(newEvent)
                Toast.makeText(
                    requireContext(),
                    R.string.success_uploading_event,
                    Toast.LENGTH_LONG
                ).show()
                findNavController().navigateUp()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            binding.tieUbicacionEvento.setText(
                UtilsKt.getAddress(UtilsKt.latitud, UtilsKt.longitud, requireContext())
            )
        } catch (e: Exception) {
            binding.tieUbicacionEvento.setText("")
        }
    }

    override fun onPause() {
        super.onPause()
        binding.tieUbicacionEvento.setText("")
    }

    private fun validateFields(): Int {
        errorCount = 0
        binding.apply {
            setError(tieNombreEvento.text.toString(), R.string.error_empty_eventName)
            setError(tieUbicacionEvento.text.toString(), R.string.error_empty_eventLocation)
            setError(tieFecha.text.toString(), R.string.error_empty_eventDate)
            setError(tieEndFecha.text.toString(), R.string.error_empty_eventDate)
            setError(tieHoraComienzoEvento.text.toString(), R.string.error_empty_eventHour)
            setError(tieHoraFinEvento.text.toString(), R.string.error_empty_eventHour)
            if (!endDate.after(startDate)) {
                Toast.makeText(context, R.string.error_date, Toast.LENGTH_SHORT).show()
                errorCount += 1
                return errorCount
            }
            setError(
                tieDescripcionEvento.text.toString(),
                R.string.error_empty_eventDescription
            )
        }
        return errorCount
    }

    private fun setError(text: String, @StringRes error: Int) {
        if (text.isBlank()) {
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            errorCount += 1
        }
    }

    private fun setStartDate() {
        val newFragment: DatePickerKt =
            DatePickerKt.newInstance { _, year, month, day -> // +1 because January is zero
                val monthFormatted = String.format("%02d", month + 1)
                val dayOfMonthFormatted = String.format("%02d", day)
                binding.tieFecha.setText("$dayOfMonthFormatted/$monthFormatted/$year")
                val fecha: Array<String> =
                    binding.tieFecha.text.toString().split("/").toTypedArray()
                diaInicio = fecha[0]
                mesInicio = fecha[1]
                anioInicio = fecha[2]
                calendar.timeInMillis = startDate.time
                calendar.set(Calendar.YEAR, anioInicio.toInt())
                calendar.set(Calendar.MONTH, mesInicio.toInt() - 1)
                calendar.set(Calendar.DAY_OF_MONTH, diaInicio.toInt())
                startDate.time = calendar.timeInMillis
            }
        newFragment.show(requireActivity().supportFragmentManager, "datePicker")
    }

    private fun setEndDate() {
        val newFragment: DatePickerKt =
            DatePickerKt.newInstance { _, year, month, day -> // +1 because January is zero
                val monthFormatted = String.format("%02d", month + 1)
                val dayOfMonthFormatted = String.format("%02d", day)
                binding.tieEndFecha.setText("$dayOfMonthFormatted/$monthFormatted/$year")

                val fecha: Array<String> =
                    binding.tieEndFecha.text.toString().split("/").toTypedArray()
                diaFin = fecha[0]
                mesFin = fecha[1]
                anioFin = fecha[2]
                calendar.timeInMillis = endDate.time
                calendar.set(Calendar.YEAR, anioFin.toInt())
                calendar.set(Calendar.MONTH, mesFin.toInt() - 1)
                calendar.set(Calendar.DAY_OF_MONTH, diaFin.toInt())
                endDate.time = calendar.timeInMillis
            }
        newFragment.show(requireActivity().supportFragmentManager, "datePicker")
    }

    private fun setStartHour() {
        val newFragment = TimePickerFragment {
            onTimeSelected(it)
            calendar.timeInMillis = startDate.time
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(it.split(":")[0]))
            calendar.set(Calendar.MINUTE, Integer.parseInt(it.split(":")[1]))
            startDate.time = calendar.timeInMillis
        }
        newFragment.show(requireActivity().supportFragmentManager, "timepicker")
    }

    private fun onTimeSelected(time: String) {
        binding.tieHoraComienzoEvento.setText(time)
    }

    private fun setEndHour() {
        val newFragment = TimePickerFragment {
            onTimeSelected2(it)
            calendar.timeInMillis = endDate.time
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(it.split(":")[0]))
            calendar.set(Calendar.MINUTE, Integer.parseInt(it.split(":")[1]))
            endDate.time = calendar.timeInMillis
        }
        newFragment.show(requireActivity().supportFragmentManager, "timepicker")
    }

    private fun onTimeSelected2(time: String) {
        binding.tieHoraFinEvento.setText(time)
    }
}
