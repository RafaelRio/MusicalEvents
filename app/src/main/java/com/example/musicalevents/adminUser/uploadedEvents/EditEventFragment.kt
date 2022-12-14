package com.example.musicalevents.adminUser.uploadedEvents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.musicalevents.R
import com.example.musicalevents.data.model.Event
import com.example.musicalevents.databinding.FragmentEditEventBinding
import com.example.musicalevents.mvp.uploadedevents.UploadedEventsContract
import com.example.musicalevents.mvp.uploadedevents.UploadedEventsPresenter
import com.example.musicalevents.utils.DatePickerKt
import com.example.musicalevents.utils.TimePickerFragment
import com.example.musicalevents.utils.UtilsKt
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

/**
 * Fragment que carga la pantalla para realizar la edición de los eventos
 * @author Rafa
 */
class EditEventFragment : Fragment(), UploadedEventsContract.View {

    private lateinit var binding: FragmentEditEventBinding
    private val args: EditEventFragmentArgs by navArgs()
    private lateinit var editedEvent: Event
    private val db = FirebaseFirestore.getInstance()
    val calendar = Calendar.getInstance()
    private var errorCount = 0
    private var startDay: String = "0"
    private var startMonth: String = "1"
    private var startYear: String = "0"
    private var endDay: String = "0"
    private var endMonth: String = "1"
    private var endYear: String = "0"
    lateinit var startDate: Date
    lateinit var endDate: Date
    private lateinit var presenter: UploadedEventsContract.Presenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editedEvent = args.editedEvent
        presenter = UploadedEventsPresenter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditEventBinding.inflate(inflater, container, false)
        binding.editTieUbicacionEvento.setText(editedEvent.location)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.editTieUbicacionEvento.setText(editedEvent.location)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startDate = Date(editedEvent.startDate)
        endDate = Date(editedEvent.endDate)

        bindingFields()
        binding.apply {

            edittieFechaInicio.setOnClickListener {
                setStartDate()
            }
            edittieFechaFin.setOnClickListener {
                setEndDate()
            }

            editTieUbicacionEvento.setOnClickListener {
                findNavController().navigate(R.id.action_editEventFragment_to_mapFragment)
            }

            edittieHoraComienzo.setOnClickListener {
                setStartHour()
            }

            edittieHoraFin.setOnClickListener {
                setEndHour()
            }

            fabEditEvento.setOnClickListener {
                if (validateFields() >= 1) {
                    return@setOnClickListener
                } else {
                    editedEvent.uuid.let { it1 ->
                        db.collection(UtilsKt.eventosTable).document(it1).set(
                            hashMapOf(
                                "uuid" to editedEvent.uuid,
                                "user" to editedEvent.user,
                                "eventName" to editTieNombreEvento.text.toString(),
                                "location" to editTieUbicacionEvento.text.toString(),
                                "startDate" to startDate.time,
                                "endDate" to endDate.time,
                                "description" to editTieDescripcionEvento.text.toString(),
                                "lat" to editedEvent.lat,
                                "lon" to editedEvent.lon
                            )
                        )
                    }
                }
                Toast.makeText(context, R.string.event_updated_successfuly, Toast.LENGTH_LONG)
                    .show()
                findNavController().navigateUp()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            binding.editTieUbicacionEvento.setText(
                UtilsKt.getAddress(UtilsKt.latitud, UtilsKt.longitud, requireContext())
            )
            editedEvent.lat = UtilsKt.latitud
            editedEvent.lon = UtilsKt.longitud
        } catch (e: Exception) {
        }
    }

    private fun bindingFields() {

        binding.apply {
            editTieNombreEvento.setText(editedEvent.eventName)
            editTieUbicacionEvento.setText(editedEvent.location)
            editTieDescripcionEvento.setText(editedEvent.description)

            calendar.timeInMillis = startDate.time
            val dayOfMonth = String.format("%02d", calendar[Calendar.DAY_OF_MONTH])
            val month = String.format("%02d", calendar[Calendar.MONTH] + 1)
            val year = String.format("%04d", calendar[Calendar.YEAR])
            val hourOfDay = String.format("%02d", calendar[Calendar.HOUR_OF_DAY])
            val minute = String.format("%02d", calendar[Calendar.MINUTE])

            edittieFechaInicio.setText("$dayOfMonth/$month/$year")
            edittieHoraComienzo.setText("$hourOfDay:$minute")

            calendar.timeInMillis = endDate.time
            val dayOfMonth2 = String.format("%02d", calendar[Calendar.DAY_OF_MONTH])
            val month2 = String.format("%02d", calendar[Calendar.MONTH] + 1)
            val year2 = String.format("%04d", calendar[Calendar.YEAR])
            val hourOfDay2 = String.format("%02d", calendar[Calendar.HOUR_OF_DAY])
            val minute2 = String.format("%02d", calendar[Calendar.MINUTE])

            edittieFechaFin.setText("$dayOfMonth2/$month2/$year2")
            edittieHoraFin.setText("$hourOfDay2:$minute2")
        }
    }

    private fun validateFields(): Int {
        errorCount = 0

        setError(binding.editTieNombreEvento.text.toString(), R.string.error_empty_eventName)
        setError(binding.editTieUbicacionEvento.text.toString(), R.string.error_empty_eventLocation)
        setError(binding.edittieFechaInicio.text.toString(), R.string.error_empty_eventDate)
        setError(binding.edittieFechaFin.text.toString(), R.string.error_empty_eventDate)
        setError(binding.edittieHoraComienzo.text.toString(), R.string.error_empty_eventHour)
        setError(binding.edittieHoraFin.text.toString(), R.string.error_empty_eventHour)
        if (!endDate.after(startDate)) {
            Toast.makeText(context, R.string.error_date, Toast.LENGTH_SHORT).show()
            errorCount += 1
            return errorCount
        }
        setError(binding.editTieDescripcionEvento.text.toString(), R.string.error_empty_eventDescription)

        return errorCount
    }

    private fun setError(text: String, @StringRes error: Int) {
        if (text.isBlank()) {
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            errorCount += 1
        }
    }

    private fun setStartDate() {
        val newFragment =
            DatePickerKt.newInstance { _, year, month, day -> // +1 because January is zero
                val monthFormatted = String.format("%02d", month + 1)
                val dayOfMonthFormatted = String.format("%02d", day)
                binding.edittieFechaInicio.setText("$dayOfMonthFormatted/$monthFormatted/$year")

                val fecha = binding.edittieFechaInicio.text.toString().split("/")
                startDay = fecha[0]
                startMonth = fecha[1]
                startYear = fecha[2]

                calendar.timeInMillis = startDate.time

                calendar.set(Calendar.YEAR, startYear.toInt())
                calendar.set(Calendar.MONTH, startMonth.toInt() - 1)
                calendar.set(Calendar.DAY_OF_MONTH, startDay.toInt())

                startDate.time = calendar.timeInMillis
            }
        newFragment.show(requireActivity().supportFragmentManager, "datePicker")
    }

    private fun setEndDate() {
        val newFragment =
            DatePickerKt.newInstance { _, year, month, day -> // +1 because January is zero
                val monthFormatted = String.format("%02d", month + 1)
                val dayOfMonthFormatted = String.format("%02d", day)
                binding.edittieFechaFin.setText("$dayOfMonthFormatted/$monthFormatted/$year")

                val fecha = binding.edittieFechaFin.text.toString().split("/")

                endDay = fecha[0]
                endMonth = fecha[1]
                endYear = fecha[2]

                calendar.timeInMillis = endDate.time

                calendar.set(Calendar.YEAR, endYear.toInt())
                calendar.set(Calendar.MONTH, endMonth.toInt() - 1)
                calendar.set(Calendar.DAY_OF_MONTH, endDay.toInt())
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
        binding.edittieHoraComienzo.setText(time)
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
        binding.edittieHoraFin.setText(time)
    }

    override fun onListSuccess(eventList: List<Event>) {
    }

    override fun onDeleteSuccess(deletedEvent: Event) {
    }

    override fun onEditSuccess(editedEvent: Event) {
    }

    override fun onNoData() {
    }
}