package com.example.musicalevents.utils

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

/**
 * Clase que instancia un DatePickerDialog
 * @author Rafa
 */

class DatePickerKt : DialogFragment(), OnDateSetListener {
    private var listener: OnDateSetListener? = null

    companion object {
        fun newInstance(listener: OnDateSetListener): DatePickerKt {
            val fragment = DatePickerKt()
            fragment.setListener(listener)
            return fragment
        }
    }

    private fun setListener(listener: OnDateSetListener?) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c[Calendar.YEAR]
        val month = c[Calendar.MONTH]
        val day = c[Calendar.DAY_OF_MONTH]

        return DatePickerDialog(requireActivity(), this, year, month, day)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, day: Int) {
        listener!!.onDateSet(view, year, month, day)
    }
}