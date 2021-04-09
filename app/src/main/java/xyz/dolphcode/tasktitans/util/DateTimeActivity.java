package xyz.dolphcode.tasktitans.util;

import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

// The Date Time Activity class is extended by activities that use the time picker
// The Date Time Activity class is how date and time pickers will send data to the activity that opened them
public abstract class DateTimeActivity extends AppCompatActivity {

    protected TextView time;
    protected TextView date;
    protected DialogFragment timePicker;
    protected DialogFragment datePicker;

    public abstract void sendDate(int day, int month, int year);
    public abstract void sendTime(int hour, int min);

    public void showTimePicker(View view) {
        timePicker.show(getSupportFragmentManager(), "taskTimePicker");
    }

    public void showDatePicker(View view) {
        datePicker.show(getSupportFragmentManager(), "taskDatePicker");
    }

}
