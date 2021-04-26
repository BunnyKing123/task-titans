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
import xyz.dolphcode.tasktitans.database.tasks.TaskGroup;
import xyz.dolphcode.tasktitans.fragments.DatePickerFragment;
import xyz.dolphcode.tasktitans.fragments.TimePickerFragment;
import xyz.dolphcode.tasktitans.util.DateTimeActivity;
import xyz.dolphcode.tasktitans.util.Util;

public class TaskCreationActivity extends DateTimeActivity {

    String ownerID;
    String targetID;
    Intent prev;
    int[] setTime;
    int[] setDate;

    boolean groupTaskCreation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_creation);

        Util.setupConnectionChangedHandler(TaskCreationActivity.this);

        Button taskBackBtn = findViewById(R.id.taskBackBtn);
        Button taskFinishBtn = findViewById(R.id.taskDoneBtn);

        prev = getIntent();

        ownerID = prev.getStringExtra("ID");
        groupTaskCreation = prev.getStringExtra("GROUPID") != null;
        targetID = prev.getStringExtra("TARGETID");

        time = findViewById(R.id.taskTimeText);
        timePicker = new TimePickerFragment();

        date = findViewById(R.id.taskDateText);
        datePicker = new DatePickerFragment();

        setTime = Util.currentTime();
        time.setText(Util.formatTime(setTime[0], setTime[1]));

        setDate = Util.currentDate();
        date.setText(Util.formatDate(setDate[0], setDate[1], setDate[2]));

        if (groupTaskCreation) {
            taskBackBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(TaskCreationActivity.this, TaskGroupPanelActivity.class);
                    intent.putExtra("GROUPID", prev.getStringExtra("GROUPID"));
                    intent.putExtra("ID", ownerID);
                    TaskCreationActivity.this.startActivity(intent);
                }
            });
        } else {
            Util.addSwitchWithUser(taskBackBtn, TasksActivity.class, TaskCreationActivity.this, ownerID);
        }

        taskFinishBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String name = ((EditText) findViewById(R.id.taskNameInputTxt)).getText().toString();
                String desc = ((EditText) findViewById(R.id.taskDescInputTxt)).getText().toString();
                int count = 1;
                try {
                    count = Integer.parseInt(((EditText) findViewById(R.id.taskCountInputTxt)).getText().toString());
                } catch (NumberFormatException e) {

                }

                if (!name.isEmpty()) {
                    Task task;
                    if (groupTaskCreation) {
                        task = Task.TaskBuilder.createGroupTask(targetID, name, Util.formatDateTimeDB(setDate[0],
                                setDate[1],
                                setDate[2],
                                setTime[0],
                                setTime[1]))
                                .setDesc(desc)
                                .setCount(count)
                                .build();
                    } else {
                        task = Task.TaskBuilder.createTask(ownerID, name, Util.formatDateTimeDB(setDate[0],
                                setDate[1],
                                setDate[2],
                                setTime[0],
                                setTime[1]))
                                .setDesc(desc)
                                .setCount(count)
                                .build();
                    }

                    Client.updateTask(task);

                    Intent intent;
                    if (groupTaskCreation) {
                        intent = new Intent(TaskCreationActivity.this, TaskGroupPanelActivity.class);
                        String groupID = prev.getStringExtra("GROUPID");
                        intent.putExtra("GROUPID", groupID);
                        TaskGroup group = Client.getTaskGroup(groupID);
                        group.addTask(prev.getStringExtra("TARGETID"), task.getTaskID());
                    } else {
                        intent = new Intent(TaskCreationActivity.this, TasksActivity.class);
                    }
                    intent.putExtra("ID", ownerID);
                    TaskCreationActivity.this.startActivity(intent);
                }
            }
        });

    }

    @Override
    public void sendDate(int day, int month, int year) {
        date.setText(Util.formatDate(day, month, year)); // Set the text for the date to show what the date is currently set to
        setDate[0] = day;
        setDate[1] = month;
        setDate[2] = year;
    }

    @Override
    public void sendTime(int hour, int min) {
        time.setText(Util.formatTime(hour, min)); // Set the text for the time to show what the time is currently set to
        setTime[0] = hour;
        setTime[1] = min;
    }
}
