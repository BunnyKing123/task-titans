package xyz.dolphcode.tasktitans;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import xyz.dolphcode.tasktitans.database.Client;
import xyz.dolphcode.tasktitans.database.User;
import xyz.dolphcode.tasktitans.database.tasks.Task;
import xyz.dolphcode.tasktitans.resources.TaskType;
import xyz.dolphcode.tasktitans.util.Util;

public class TasksActivity extends AppCompatActivity {

    int tSelection = 0;
    int rSelection = 0;
    TextView taskText;
    ArrayList<Task> tasks;
    ArrayList<Task> repeatTasks = new ArrayList<Task>();
    Task selectedTask;
    Task selectedRptTask;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        user = Client.getUser(getIntent().getStringExtra("ID"));

        Button tasksBtn = findViewById(R.id.addTaskBtn);
        Button rptTasksBtn = findViewById(R.id.addRepeatTaskBtn);
        Button groupTasksBtn = findViewById(R.id.groupTasksBtn);
        Button backBtn = findViewById(R.id.questsBackBtn);
        Button taskFinBtn = findViewById(R.id.completeTaskBtn);

        ImageButton taskLeft = findViewById(R.id.taskLeftBtn);
        ImageButton taskRight = findViewById(R.id.taskRightBtn);

        taskText = findViewById(R.id.taskText);

        tasks = Client.getTasksByUser(getIntent().getStringExtra("ID"));

        addTaskSelection(taskLeft, taskRight, TaskType.TASK);
        setTaskText();

        taskFinBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (tasks.size() > 0) {
                    Task task = tasks.get(tSelection);
                    if (task.finish()) {
                        tasks.remove(task);
                        checkSelection();
                        setTaskText();
                        user.addRewards(50, 50, 20);
                    }
                }
            }
        });

        Util.addSwitchWithUser(tasksBtn, TaskCreationActivity.class, TasksActivity.this, getIntent().getStringExtra("ID"));
        Util.addSwitchWithUser(rptTasksBtn, RepeatTaskActivity.class, TasksActivity.this, getIntent().getStringExtra("ID"));
        Util.addSwitchScreenAction(groupTasksBtn, GroupTaskActivity.class, TasksActivity.this);
        Util.addSwitchWithUser(backBtn, GameActivity.class, TasksActivity.this, getIntent().getStringExtra("ID"));
    }

    private void setTaskText() {
        checkSelection();

        if (tasks.size() > 0) {
            taskText.setText(tasks.get(tSelection).getTaskName());
        } else {
            taskText.setText("Your task board is empty");
        }
    }

    // Add all necessary listeners for task selection for scrolling through tasks
    // Task type 0 is normal
    // Task type 1 is repeat
    private void addTaskSelection(ImageButton leftBtn, ImageButton rightBtn, final int type) {
        leftBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeSelection(type, -1);
                checkSelection();

                setTaskText();
            }
        });

        rightBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ArrayList<Task> list = getList(type);
                changeSelection(type, 1);
                checkSelection();

                setTaskText();
            }
        });
    }

    // Change either tSelection or rSelection depending on whether we are changing selection for repeat or normal tasks
    // 0 represents normal tasks
    // 1 represents repeat tasks
    private void changeSelection(int type, int change) {
        if (type == TaskType.TASK) {
            tSelection += change;
        } else {
            rSelection += change;
        }
    }

    // Set either tSelection or rSelection depending on whether we are setting selection for repeat or normal tasks
    // 0 represents normal tasks
    // 1 represents repeat tasks
    private void setSelection(int type, int set) {
        if (type == TaskType.TASK) {
            tSelection = set;
        } else {
            rSelection = set;
        }
    }

    // Get either tSelection or rSelection depending on whether we are getting selection for repeat or normal tasks
    // 0 represents normal tasks
    // 1 represents repeat tasks
    private int getSelection(int type) {
        if (type == TaskType.TASK) {
            return tSelection;
        } else {
            return rSelection;
        }
    }

    // Refer to either the task list or repeat task list depending on whether we are getting selection for repeat or normal tasks
    // 0 represents normal tasks
    // 1 represents repeat tasks
    private ArrayList<Task> getList(int type) {
        if (type == TaskType.TASK) {
            return tasks;
        } else {
            return repeatTasks;
        }
    }

    // Checks and clamps selection values
    private void checkSelection() {
        if (tSelection >= tasks.size())
            tSelection = tasks.size() - 1;

        if (tSelection < 0)
            tSelection = 0;

        if (rSelection >= repeatTasks.size())
            rSelection = repeatTasks.size() - 1;

        if (rSelection < 0)
            rSelection = 0;
    }

}
