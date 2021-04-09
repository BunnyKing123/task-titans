package xyz.dolphcode.tasktitans;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import xyz.dolphcode.tasktitans.fragments.DatePickerFragment;
import xyz.dolphcode.tasktitans.fragments.TimePickerFragment;
import xyz.dolphcode.tasktitans.util.DateTimeActivity;
import xyz.dolphcode.tasktitans.util.Util;

public class TaskCreationActivity extends DateTimeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_creation);

        Button taskBackBtn = findViewById(R.id.taskBackBtn);

        time = findViewById(R.id.taskTimeText);
        timePicker = new TimePickerFragment();

        date = findViewById(R.id.taskDateText);
        datePicker = new DatePickerFragment();

        int[] currentTime = Util.currentTime();
        time.setText(Util.formatTime(currentTime[0], currentTime[1]));

        int[] currentDate = Util.currentDate();
        date.setText(Util.formatDate(currentDate[0], currentDate[1], currentDate[2]));

        Util.addSwitchScreenAction(taskBackBtn, TasksActivity.class, TaskCreationActivity.this);
    }

    @Override
    public void sendDate(int day, int month, int year) { date.setText(Util.formatDate(day, month, year)); }

    @Override
    public void sendTime(int hour, int min) {
        time.setText(Util.formatTime(hour, min));
    }
}
