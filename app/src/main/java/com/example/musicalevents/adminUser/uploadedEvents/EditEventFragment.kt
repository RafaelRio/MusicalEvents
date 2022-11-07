package com.example.musicalevents.adminUser.uploadedEvents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.musicalevents.R
import com.example.musicalevents.data.model.Event
import com.example.musicalevents.data.repository.LoginRepository
import com.example.musicalevents.databinding.FragmentEditEventBinding
import com.example.musicalevents.utils.DatePickerFragment
import com.example.musicalevents.utils.TimePickerFragment
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class EditEventFragment : Fragment(), UploadedEventsContract.View {

    private lateinit var binding: FragmentEditEventBinding
    private val args: EditEventFragmentArgs by navArgs()
    private lateinit var editedEvent: Event
    private val db = FirebaseFirestore.getInstance()
    val startCalendar = Calendar.getInstance()
    val endCalendar = Calendar.getInstance()
    private var errorCount = 0
    var diaInicio: String = "0"
    var mesInicio: String = "0"
    var anioInicio: String = "0"
    var diaFin: String = "0"
    var mesFin: String = "0"
    var anioFin: String = "0"
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
        //bindingFields()
        binding.apply {

            edittieFechaInicio.setOnClickListener {
                showDatePickerDialog()
            }
            edittieFechaFin.setOnClickListener {
                showDatePickerDialog2()
            }

            edittieHoraComienzo.setOnClickListener {
                showTimePickerDialog()
            }

            edittieHoraFin.setOnClickListener {
                showTimePickerDialog2()
            }

            fabEditEvento.setOnClickListener {
                if (validateFields() >= 1) {
                    return@setOnClickListener
                } else {
                    editedEvent.uuid?.let { it1 ->
                        db.collection("eventos").document(it1).set(
                            hashMapOf(
                                "uuid" to editedEvent.uuid,
                                "user" to editedEvent.user,
                                "nombreEvento" to editTieNombreEvento.text.toString(),
                                "ubicacion" to editTieUbicacionEvento.text.toString(),
                                "diaInicio" to edittieFechaInicio.text.toString().split("/")[0],
                                "mesInicio" to edittieFechaInicio.text.toString().split("/")[1],
                                "anioInicio" to edittieFechaInicio.text.toString().split("/")[2],
                                "horaComienzo" to edittieHoraComienzo.text.toString(),
                                "diaFin" to edittieFechaFin.text.toString().split("/")[0],
                                "mesFin" to edittieFechaFin.text.toString().split("/")[1],
                                "anioFin" to edittieFechaFin.text.toString().split("/")[2],
                                "horaFin" to edittieHoraFin.text.toString(),
                                "descripcion" to editTieDescripcionEvento.text.toString()
                            )
                        )
                    }
                }
                Toast.makeText(context, R.string.event_updated_successfuly, Toast.LENGTH_LONG).show()
                findNavController().navigateUp()
            }
        }

    }

//    private fun bindingFields() {
//
//        val fechaInicio =
//            editedEvent.diaInicio + "/" + editedEvent.mesInicio + "/" + editedEvent.anioInicio
//
//        val fechaFin =
//            editedEvent.diaFin + "/" + editedEvent.mesFin + "/" + editedEvent.anioFin
//
//        binding.apply {
//            editTieNombreEvento.setText(editedEvent.nombreEvento)
//            editTieUbicacionEvento.setText(editedEvent.ubicacion)
//            edittieFechaInicio.setText(fechaInicio)
//            edittieFechaFin.setText(fechaFin)
//            edittieHoraComienzo.setText(editedEvent.horaComienzo)
//            edittieHoraFin.setText(editedEvent.horaFin)
//            editTieDescripcionEvento.setText(editedEvent.descripcion)
//        }
//        editedEvent.user = LoginRepository.currentUser
//
//    }

    private fun validateFields(): Int {
        errorCount = 0

        val startDate: Date = startCalendar.time
        val endDate: Date = endCalendar.time

        if (binding.editTieNombreEvento.text?.isBlank() == true) {
            Toast.makeText(context, R.string.error_empty_eventName, Toast.LENGTH_SHORT).show()
            errorCount += 1
            return errorCount
        }

        if (binding.editTieUbicacionEvento.text?.isBlank() == true) {
            Toast.makeText(context, R.string.error_empty_eventLocation, Toast.LENGTH_SHORT).show()
            errorCount += 1
            return errorCount
        }

        if (binding.edittieFechaInicio.text?.isBlank() == true) {
            Toast.makeText(context, R.string.error_empty_eventDate, Toast.LENGTH_SHORT).show()
            errorCount += 1
            return errorCount
        }

        if (binding.edittieFechaFin.text?.isBlank() == true) {
            Toast.makeText(context, R.string.error_empty_eventDate, Toast.LENGTH_SHORT).show()
            errorCount += 1
            return errorCount
        }

        if (binding.edittieHoraComienzo.text?.isBlank() == true) {
            Toast.makeText(context, R.string.error_empty_eventHour, Toast.LENGTH_SHORT).show()
            errorCount += 1
            return errorCount
        }

        if (binding.edittieHoraFin.text?.isBlank() == true) {
            Toast.makeText(context, R.string.error_empty_eventHour, Toast.LENGTH_SHORT).show()
            errorCount += 1
            return errorCount
        }

        if (!endDate.after(startDate)) {
            Toast.makeText(context, R.string.error_date, Toast.LENGTH_SHORT).show()
            errorCount += 1
            return errorCount
        }

        if (binding.editTieDescripcionEvento.text?.isBlank() == true) {
            Toast.makeText(context, R.string.error_empty_eventDescription, Toast.LENGTH_SHORT).show()
            errorCount += 1
            return errorCount
        }

        return errorCount
    }

    private fun showDatePickerDialog() {
        val newFragment: DatePickerFragment =
            DatePickerFragment.newInstance { _, year, month, day -> // +1 because January is zero
                val monthFormatted = String.format("%02d", month + 1)
                val dayOfMonthFormatted = String.format("%02d", day)
                binding.edittieFechaInicio.setText("$dayOfMonthFormatted/$monthFormatted/$year")

                val fecha: Array<String> =
                    binding.edittieFechaInicio.text.toString().split("/").toTypedArray()
                diaInicio = fecha[0]
                mesInicio = fecha[1]
                anioInicio = fecha[2]

                startCalendar.set(Calendar.YEAR, Integer.parseInt(anioInicio))
                startCalendar.set(Calendar.MONTH, Integer.parseInt(mesInicio))
                startCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(diaInicio))
            }
        newFragment.show(requireActivity().supportFragmentManager, "datePicker")
    }

    private fun showDatePickerDialog2() {
        val newFragment: DatePickerFragment =
            DatePickerFragment.newInstance { _, year, month, day -> // +1 because January is zero
                val monthFormatted = String.format("%02d", month + 1)
                val dayOfMonthFormatted = String.format("%02d", day)
                binding.edittieFechaFin.setText("$dayOfMonthFormatted/$monthFormatted/$year")

                val fecha: Array<String> =
                    binding.edittieFechaFin.text.toString().split("/").toTypedArray()
                diaFin = fecha[0]
                mesFin = fecha[1]
                anioFin = fecha[2]

                endCalendar.set(Calendar.YEAR, Integer.parseInt(anioFin))
                endCalendar.set(Calendar.MONTH, Integer.parseInt(mesFin))
                endCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(diaFin))
            }
        newFragment.show(requireActivity().supportFragmentManager, "datePicker")
    }

    private fun showTimePickerDialog() {
        val newFragment = TimePickerFragment {
            onTimeSelected(it)
            startCalendar.set(Calendar.HOUR, Integer.parseInt(it.split(":")[0]))
            startCalendar.set(Calendar.MINUTE, Integer.parseInt(it.split(":")[1]))
        }
        newFragment.show(requireActivity().supportFragmentManager, "timepicker")
    }

    private fun onTimeSelected(time: String) {
        binding.edittieHoraComienzo.setText(time)
    }

    private fun showTimePickerDialog2() {
        val newFragment = TimePickerFragment {
            onTimeSelected2(it)
            endCalendar.set(Calendar.HOUR, Integer.parseInt(it.split(":")[0]))
            endCalendar.set(Calendar.MINUTE, Integer.parseInt(it.split(":")[1]))
        }
        newFragment.show(requireActivity().supportFragmentManager, "timepicker")
    }

    private fun onTimeSelected2(time: String) {
        binding.edittieHoraFin.setText(time)
    }

    override fun showProgress() {
    }

    override fun hideProgress() {
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