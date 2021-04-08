package xyz.dolphcode.tasktitans;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import xyz.dolphcode.tasktitans.fragments.TimePickerFragment;
import xyz.dolphcode.tasktitans.util.DateTimeActivity;
import xyz.dolphcode.tasktitans.util.Util;

public class TaskCreationActivity extends AppCompatActivity implements DateTimeActivity {

    TextView time;
    DialogFragment timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_creation);

        Button taskBackBtn = findViewById(R.id.taskBackBtn);

        time = findViewById(R.id.taskTimeText);
        timePicker = new TimePickerFragment();

        int[] currentTime = Util.currentTime();
        time.setText(Util.formatTime(currentTime[0], currentTime[1]));

        Util.addSwitchScreenAction(taskBackBtn, TasksActivity.class, TaskCreationActivity.this);
    }

    public void showTimePicker(View view) {
        timePicker.show(getSupportFragmentManager(), "taskTimePicker");
    }

    @Override
    public void sendDate() {}

    @Override
    public void sendTime(int hour, int min) {
        time.setText(Util.formatTime(hour, min));
    }
}
