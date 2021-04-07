package xyz.dolphcode.tasktitans;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import xyz.dolphcode.tasktitans.util.Util;

public class TaskCreationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_creation);

        Button taskBackBtn = findViewById(R.id.taskBackBtn);

        Util.addSwitchScreenAction(taskBackBtn, TasksActivity.class, TaskCreationActivity.this);
    }
}
