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

// The RepeatTaskActivity class is linked to the repeat task screen where users can create Repeat Tasks
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

        Util.setupConnectionChangedHandler(RepeatTaskActivity.this);

        prev = getIntent(); // Store intent being passed on
        id = prev.getStringExtra("ID"); // Store player id

        // Get references to components in the screen
        Button taskBackBtn = findViewById(R.id.rptTaskBackBtn);
        Button freqBtn = findViewById(R.id.rptTaskFreqBtn);
        Button doneBtn = findViewById(R.id.rptTaskDoneBtn);

        nameInput = findViewById(R.id.rptTaskNameInputTxt);
        descInput = findViewById(R.id.rptTaskDescInputTxt);
        countInput = findViewById(R.id.rptTaskCountInputTxt);

        // Check if frequency data has been provided meaning the user has navigated from this screen to the frequency data screen and back here
        if (!(prev.getStringExtra("FREQDATA") == null)) {
            nameInput.setText(prev.getStringExtra("NAME"), TextView.BufferType.EDITABLE);
            descInput.setText(prev.getStringExtra("DESC"), TextView.BufferType.EDITABLE);
            countInput.setText(prev.getIntExtra("COUNT", 1) + "", TextView.BufferType.EDITABLE);
            freqType = prev.getIntExtra("FREQTYPE", -1);
            freqData = prev.getStringExtra("FREQDATA");

        }

        Util.addSwitchWithUser(taskBackBtn, TasksActivity.class, RepeatTaskActivity.this, id);

        // Transfers data to the frequency screen to be sent back to this screen along with the needed frequency data
        freqBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(android.view.View v) {
                Intent intent = new Intent(RepeatTaskActivity.this, FrequencyPickerActivity.class);

                int count;
                count = Util.safeParseInt(countInput.getText().toString(), 1);

                String name = nameInput.getText().toString();
                String desc = descInput.getText().toString();

                // Add data to be transferred to the frequency screen
                intent.putExtra("ID", id);
                intent.putExtra("NAME", name);
                intent.putExtra("COUNT", count);
                intent.putExtra("DESC", desc);

                // Switch to the frequency selection screen
                RepeatTaskActivity.this.startActivity(intent);
            }
        });

        // Adds repeat task to the database
        doneBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(android.view.View v) {
                String name = nameInput.getText().toString();

                // Check to make sure all necessary fields are filled out and frequency data is provided
                if (!name.isEmpty() && prev.getStringExtra("FREQDATA") != null) {
                    Intent intent = new Intent(RepeatTaskActivity.this, TasksActivity.class);

                    int count;
                    count = Util.safeParseInt(countInput.getText().toString(), 1);

                    String desc = descInput.getText().toString();

                    // Create the task to be stored
                    Task task = Task.TaskBuilder.createRepeatTask(id, name, freqData, freqType)
                            .setCount(count)
                            .setDesc(desc)
                            .build();

                    // Store the task in the database
                    Client.updateTask(task);

                    intent.putExtra("ID", id);

                    // Switch back to the tasks screen
                    RepeatTaskActivity.this.startActivity(intent);
                }

            }
        });
    }
}
