package xyz.dolphcode.tasktitans.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;

import xyz.dolphcode.tasktitans.util.DateTimeActivity;
import xyz.dolphcode.tasktitans.util.Util;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    int hour;
    int minute;

    public TimePickerFragment() {
        super();
        int[] currentTime = Util.currentTime();
        hour = currentTime[0];
        minute = currentTime[1];
    }

    // Code based on code from Android Studio docs on Time Picker fragment
    @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Activity screen = getActivity();
        if (screen instanceof DateTimeActivity) {
            ((DateTimeActivity) screen).sendTime(hourOfDay, minute);
        }
    }

}
