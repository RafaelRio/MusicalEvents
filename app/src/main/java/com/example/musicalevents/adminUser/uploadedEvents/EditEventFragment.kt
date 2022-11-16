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
    var diaInicio: String = "0"
    var mesInicio: String = "1"
    var anioInicio: String = "0"
    var diaFin: String = "0"
    var mesFin: String = "1"
    var anioFin: String = "0"
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
        return binding.root
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
                                "nombreEvento" to editTieNombreEvento.text.toString(),
                                "ubicacion" to editTieUbicacionEvento.text.toString(),
                                "fechaInicioMiliSegundos" to startDate.time,
                                "fechaFinMiliSegundos" to endDate.time,
                                "descripcion" to editTieDescripcionEvento.text.toString()
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
                diaInicio = fecha[0]
                mesInicio = fecha[1]
                anioInicio = fecha[2]

                calendar.timeInMillis = startDate.time

                UtilsKt.calendarSetDate(calendar = calendar, anio = anioInicio, mes = mesInicio, dia = diaInicio)

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

                diaFin = fecha[0]
                mesFin = fecha[1]
                anioFin = fecha[2]

                calendar.timeInMillis = endDate.time

                UtilsKt.calendarSetDate(calendar = calendar, anio = anioFin, mes = mesFin, dia = diaFin)
                endDate.time = calendar.timeInMillis
            }
        newFragment.show(requireActivity().supportFragmentManager, "datePicker")
    }

    private fun setStartHour() {
        val newFragment = TimePickerFragment {
            onTimeSelected(it)

            calendar.timeInMillis = startDate.time

            UtilsKt.calendarSetHour(calendar = calendar, horaMinuto = it)

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
            UtilsKt.calendarSetHour(calendar = calendar, horaMinuto = it)

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