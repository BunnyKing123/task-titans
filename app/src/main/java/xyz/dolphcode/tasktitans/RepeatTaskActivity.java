package xyz.dolphcode.tasktitans;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import xyz.dolphcode.tasktitans.util.Util;

public class RepeatTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repeat_task);

        Button taskBackBtn = findViewById(R.id.rptTaskBackBtn);

        Util.addSwitchScreenAction(taskBackBtn, TasksActivity.class, RepeatTaskActivity.this);
    }
}
