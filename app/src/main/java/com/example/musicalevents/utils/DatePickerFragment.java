package com.example.musicalevents.utils;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    private DatePickerDialog.OnDateSetListener listener;

    public static DatePickerFragment newInstance(DatePickerDialog.OnDateSetListener listener) {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setListener(listener);
        return fragment;
    }

    public void setListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }


    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        listener.onDateSet(view, year, month, day);
    }

}

