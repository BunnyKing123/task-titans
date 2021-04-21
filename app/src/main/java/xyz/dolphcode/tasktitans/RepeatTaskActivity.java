package xyz.dolphcode.tasktitans;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import xyz.dolphcode.tasktitans.database.Client;
import xyz.dolphcode.tasktitans.database.tasks.Task;
import xyz.dolphcode.tasktitans.util.Util;

public class RepeatTaskActivity extends AppCompatActivity {

    String id;
    int freqType = -1;
    String freqData;


    EditText nameInput;
    EditText countInput;
    EditText descInput;

    Intent prev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repeat_task);

        prev = getIntent();
        id = prev.getStringExtra("ID");

        Button taskBackBtn = findViewById(R.id.rptTaskBackBtn);
        Button freqBtn = findViewById(R.id.rptTaskFreqBtn);
        Button doneBtn = findViewById(R.id.rptTaskDoneBtn);

        nameInput = findViewById(R.id.rptTaskNameInputTxt);
        descInput = findViewById(R.id.rptTaskDescInputTxt);
        countInput = findViewById(R.id.rptTaskCountInputTxt);

        if (!(prev.getStringExtra("FREQDATA") == null)) {
            Log.v("TEST", prev.getStringExtra("FREQDATA"));
            nameInput.setText(prev.getStringExtra("NAME"), TextView.BufferType.EDITABLE);
            descInput.setText(prev.getStringExtra("DESC"), TextView.BufferType.EDITABLE);
            countInput.setText(prev.getIntExtra("COUNT", 1) + "", TextView.BufferType.EDITABLE);
            freqType = prev.getIntExtra("FREQTYPE", -1);
            freqData = prev.getStringExtra("FREQDATA");

        }

        Util.addSwitchWithUser(taskBackBtn, TasksActivity.class, RepeatTaskActivity.this, id);

        freqBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(android.view.View v) {
                Intent intent = new Intent(RepeatTaskActivity.this, FrequencyPickerActivity.class);

                int count;
                try {
                    count = Integer.parseInt(countInput.getText().toString());
                } catch (NumberFormatException e) {
                    count = 1;
                }

                String name = nameInput.getText().toString();
                String desc = descInput.getText().toString();

                intent.putExtra("ID", id);
                intent.putExtra("NAME", name);
                intent.putExtra("COUNT", count);
                intent.putExtra("DESC", desc);

                RepeatTaskActivity.this.startActivity(intent);
            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(android.view.View v) {
                String name = nameInput.getText().toString();
                if (!name.isEmpty() && prev.getStringExtra("FREQDATA") != null) {
                    Intent intent = new Intent(RepeatTaskActivity.this, TasksActivity.class);

                    int count;
                    try {
                        count = Integer.parseInt(countInput.getText().toString());
                    } catch (NumberFormatException e) {
                        count = 1;
                    }


                    String desc = descInput.getText().toString();

                    Task task = Task.TaskBuilder.createRepeatTask(id, name, freqData, freqType)
                            .setCount(count)
                            .setDesc(desc)
                            .build();

                    Client.updateTask(task);

                    intent.putExtra("ID", id);
                    RepeatTaskActivity.this.startActivity(intent);
                }

            }
        });
    }
}
