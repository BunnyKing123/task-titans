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

// The TimePickerFragment class is used to represent the dialogue for selecting a time
// Fragments are reusable components or views that can be used alongside activities
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    int hour; // Used to store the hour set on the time picker so that the last time set remains on the time picker
    int minute; // Same reasoning as for the hour variable but for minutes of the hour

    public TimePickerFragment() {
        super(); // Call super constructor before anything else just in case

        // Set default time on time picker to the current time
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

    // Send the time that was picked to the activity that created the dialog
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Activity screen = getActivity();
        if (screen instanceof DateTimeActivity) { // If the activity extends DateTimeActivity it will receive data about the hour of day and minute
            ((DateTimeActivity) screen).sendTime(hourOfDay, minute);
        }
    }

}
