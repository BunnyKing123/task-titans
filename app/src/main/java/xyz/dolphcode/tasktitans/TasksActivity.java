package xyz.dolphcode.tasktitans;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import xyz.dolphcode.tasktitans.util.Util;

public class TasksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        Button tasksBtn = findViewById(R.id.addTaskBtn);
        Button rptTasksBtn = findViewById(R.id.addRepeatTaskBtn);
        Button groupTasksBtn = findViewById(R.id.groupTasksBtn);

        Util.addSwitchScreenAction(tasksBtn, TaskCreationActivity.class, TasksActivity.this);
        Util.addSwitchScreenAction(rptTasksBtn, RepeatTaskActivity.class, TasksActivity.this);
        Util.addSwitchScreenAction(groupTasksBtn, GroupTaskActivity.class, TasksActivity.this);
    }
}
