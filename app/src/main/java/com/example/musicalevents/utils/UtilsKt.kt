package com.example.musicalevents.utils

import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import java.util.*
import java.util.regex.Pattern

class UtilsKt {



    companion object{
        val eventosTable = "eventos"
        val personasTable = "personas"
        fun setDateHour(text: TextInputEditText, hour: TextInputEditText, calendar: Calendar) {
            text.setText(
                String.format("%02d", calendar[Calendar.DAY_OF_MONTH]) + "/" + String.format(
                    "%02d",
                    calendar[Calendar.MONTH] + 1
                ) + "/" + String.format(
                    "%04d",
                    calendar[Calendar.YEAR]
                )
            )
            hour.setText(
                (String.format("%02d", calendar[Calendar.HOUR_OF_DAY]) + ":" + String.format(
                    "%02d",
                    calendar[Calendar.MINUTE]
                ))
            )
        }

        fun setDateHour(text: TextView, hour: TextView, calendar: Calendar) {
            text.text = String.format("%02d", calendar[Calendar.DAY_OF_MONTH]) + "/" + String.format(
                "%02d",
                calendar[Calendar.MONTH] + 1
            ) + "/" + String.format(
                "%04d",
                calendar[Calendar.YEAR]
            )
            hour.text = (String.format("%02d", calendar[Calendar.HOUR_OF_DAY]) + ":" + String.format(
                "%02d",
                calendar[Calendar.MINUTE]
            ))
        }

        @JvmStatic
        fun isPasswordValid(password: String) : Boolean{
            val PASSWORDPATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$"
            val pattern = Pattern.compile(PASSWORDPATTERN);
            val matcher = pattern.matcher(password);
            return matcher.matches();
        }

    }

}