package xyz.dolphcode.tasktitans;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import xyz.dolphcode.tasktitans.util.Util;

public class GroupTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_task);

        Button taskBackBtn = findViewById(R.id.groupBackBtn);

        Util.addSwitchScreenAction(taskBackBtn, TasksActivity.class, GroupTaskActivity.this);
    }
}
