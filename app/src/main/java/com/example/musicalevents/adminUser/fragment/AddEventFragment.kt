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

class AddEventFragment : Fragment() {

    private lateinit var binding : FragmentAddEventBinding
    private lateinit var eventRepository : JavaEventRepository
    var e = Event()
    var dia: Int = 0
    var mes: Int = 0
    var anio: Int = 0
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
            e.dia = dia
            e.mes = mes
            e.anio = anio
            e.horaComienzo = binding.tieHoraComienzoEvento.text.toString()
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
        if(binding.tieNombreEvento.text?.isBlank() == true){
            Toast.makeText(context, "Nombre vacio", Toast.LENGTH_SHORT).show()
            errorCount+=1
            return errorCount
        }

        if(binding.tieUbicacionEvento.text?.isBlank() == true){
            Toast.makeText(context, "Ubicacion vacia vacio", Toast.LENGTH_SHORT).show()
            errorCount+=1
            return errorCount
        }

        if(binding.tieFecha.text?.isBlank() == true){
            Toast.makeText(context, "Fecha vacia", Toast.LENGTH_SHORT).show()
            errorCount+=1
            return errorCount
        }

        if(binding.tieNombreEvento.text?.isBlank() == true){
            Toast.makeText(context, "Nombre vacio", Toast.LENGTH_SHORT).show()
            errorCount+=1
            return errorCount
        }

        if(binding.tieHoraComienzoEvento.text?.isBlank() == true){
            Toast.makeText(context, "Hora comienzo vacio", Toast.LENGTH_SHORT).show()
            errorCount+=1
            return errorCount
        }

        if(binding.tieHoraFinEvento.text?.isBlank() == true){
            Toast.makeText(context, "Hora fin vacio", Toast.LENGTH_SHORT).show()
            errorCount+=1
            return errorCount
        }

        if (Integer.parseInt(binding.tieHoraComienzoEvento.text.toString().split(":")[0]) >
            Integer.parseInt(binding.tieHoraFinEvento.text.toString().split(":")[0])){
            Toast.makeText(context, "La hora de fin debe ser posterior a la hora de comienzo", Toast.LENGTH_SHORT).show()
            errorCount += 1
            return errorCount
        }

        if(binding.tieDescripcionEvento.text?.isBlank() == true){
            Toast.makeText(context, "Descripcion vacio", Toast.LENGTH_SHORT).show()
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
                dia = Integer.parseInt(fecha[0])
                mes = Integer.parseInt(fecha[1])
                anio = Integer.parseInt(fecha[2])
            }
        newFragment.show(requireActivity().supportFragmentManager, "datePicker")
    }

    private fun showTimePickerDialog() {
        val newFragment = TimePickerFragment{
            onTimeSelected(it)
        }
        newFragment.show(requireActivity().supportFragmentManager, "timepicker")
    }

    private fun onTimeSelected(time: String) {
        binding.tieHoraComienzoEvento.setText(time)
    }

    private fun showTimePickerDialog2() {
        val newFragment = TimePickerFragment{
            onTimeSelected2(it)
        }
        newFragment.show(requireActivity().supportFragmentManager, "timepicker")
    }

    private fun onTimeSelected2(time: String) {
        binding.tieHoraFinEvento.setText(time)
    }

}