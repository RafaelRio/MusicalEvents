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
        binding.editTieUbicacionEvento.setText(editedEvent.ubicacion)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.editTieUbicacionEvento.setText(editedEvent.ubicacion)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startDate = Date(editedEvent.fechaInicioMiliSegundos)
        endDate = Date(editedEvent.fechaFinMiliSegundos)

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

            //Todo incluir lat y lon a editar el evento

            fabEditEvento.setOnClickListener {
                if (validateFields() >= 1) {
                    return@setOnClickListener
                } else {
                    editedEvent.uuid.let { it1 ->
                        db.collection(UtilsKt.eventosTable).document(it1).set(
                            hashMapOf(
                                "uuid" to editedEvent.uuid,
                                "user" to editedEvent.user,
                                "nombreEvento" to editTieNombreEvento.text.toString(),
                                "ubicacion" to editTieUbicacionEvento.text.toString(),
                                "fechaInicioMiliSegundos" to startDate.time,
                                "fechaFinMiliSegundos" to endDate.time,
                                "descripcion" to editTieDescripcionEvento.text.toString(),
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
            //binding.editTieUbicacionEvento.setText("")
        }
    }

    override fun onPause() {
        super.onPause()
        //binding.editTieUbicacionEvento.setText("")
    }

    private fun bindingFields() {

        binding.apply {
            editTieNombreEvento.setText(editedEvent.nombreEvento)
            editTieUbicacionEvento.setText(editedEvent.ubicacion)
            editTieDescripcionEvento.setText(editedEvent.descripcion)

            calendar.timeInMillis = startDate.time
            UtilsKt.setDateHour(edittieFechaInicio, edittieHoraComienzo, calendar)

            calendar.timeInMillis = endDate.time
            UtilsKt.setDateHour(edittieFechaFin, edittieHoraFin, calendar)
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

                UtilsKt.setDate(calendar = calendar, anio = startYear, mes = startMonth, dia = startDay)

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

                UtilsKt.setDate(calendar = calendar, anio = endYear, mes = endMonth, dia = endDay)
                endDate.time = calendar.timeInMillis
            }
        newFragment.show(requireActivity().supportFragmentManager, "datePicker")
    }

    private fun setStartHour() {
        val newFragment = TimePickerFragment {
            onTimeSelected(it)

            calendar.timeInMillis = startDate.time

            UtilsKt.setHour(calendar = calendar, horaMinuto = it)

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
            UtilsKt.setHour(calendar = calendar, horaMinuto = it)

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