package xyz.dolphcode.tasktitans.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;

import xyz.dolphcode.tasktitans.util.DateTimeActivity;
import xyz.dolphcode.tasktitans.util.Util;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    int day; // Used to store the day of the month so that when the date picker is opened the date remains the same as what was last picked
    int month; // Same as above but for storing the month selected
    int year; // Same as above but for storing the year selected

    public DatePickerFragment() {
        super(); // Call to super constructor before performing any logic

        // Set default date to today's date
        int[] date = Util.currentDate();
        day = date[0];
        month = date[1];
        year = date[2];
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    // Send the date that was picked to the activity that created the dialog
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Activity screen = getActivity();
        if (screen instanceof DateTimeActivity) { // If the activity extends DateTimeActivity it will receive data about the hour of day and minute
            ((DateTimeActivity) screen).sendDate(day, month, year);
        }
    }

}
