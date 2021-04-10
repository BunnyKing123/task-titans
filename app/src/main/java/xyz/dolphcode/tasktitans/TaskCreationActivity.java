package xyz.dolphcode.tasktitans;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import xyz.dolphcode.tasktitans.database.Client;
import xyz.dolphcode.tasktitans.database.tasks.Task;
import xyz.dolphcode.tasktitans.fragments.DatePickerFragment;
import xyz.dolphcode.tasktitans.fragments.TimePickerFragment;
import xyz.dolphcode.tasktitans.util.DateTimeActivity;
import xyz.dolphcode.tasktitans.util.Util;

public class TaskCreationActivity extends DateTimeActivity {

    String ownerID;
    int[] currentTime;
    int[] currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_creation);

        Button taskBackBtn = findViewById(R.id.taskBackBtn);
        Button taskFinishBtn = findViewById(R.id.taskDoneBtn);

        ownerID = getIntent().getStringExtra("ID");

        time = findViewById(R.id.taskTimeText);
        timePicker = new TimePickerFragment();

        date = findViewById(R.id.taskDateText);
        datePicker = new DatePickerFragment();

        currentTime = Util.currentTime();
        time.setText(Util.formatTime(currentTime[0], currentTime[1]));

        currentDate = Util.currentDate();
        date.setText(Util.formatDate(currentDate[0], currentDate[1], currentDate[2]));

        Util.addSwitchScreenAction(taskBackBtn, TasksActivity.class, TaskCreationActivity.this);

        taskFinishBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String name = ((EditText) findViewById(R.id.taskNameInputTxt)).getText().toString();
                String desc = ((EditText) findViewById(R.id.taskDescInputTxt)).getText().toString();
                int count =  Integer.parseInt(((EditText) findViewById(R.id.taskCountInputTxt)).getText().toString());

                Log.v("ID", ownerID);

                if (!name.isEmpty()) {
                    Task task = Task.TaskBuilder.createTask(ownerID, name, Util.formatDateTimeDB(currentDate[0],
                            currentDate[1],
                            currentDate[2],
                            currentTime[0],
                            currentTime[1]))
                            .setDesc(desc)
                            .setCount(count)
                            .build();

                    Client.updateTask(task);

                    Intent intent = new Intent(TaskCreationActivity.this, TasksActivity.class);
                    intent.putExtra("ID", ownerID);
                    TaskCreationActivity.this.startActivity(intent);
                }
            }
        });

    }

    @Override
    public void sendDate(int day, int month, int year) { date.setText(Util.formatDate(day, month, year)); }

    @Override
    public void sendTime(int hour, int min) {
        time.setText(Util.formatTime(hour, min));
    }
}
