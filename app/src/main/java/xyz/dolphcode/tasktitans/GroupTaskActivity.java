package xyz.dolphcode.tasktitans;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.security.acl.Group;
import java.util.ArrayList;

import xyz.dolphcode.tasktitans.database.Client;
import xyz.dolphcode.tasktitans.database.DatabaseObserver;
import xyz.dolphcode.tasktitans.database.User;
import xyz.dolphcode.tasktitans.database.tasks.GroupMember;
import xyz.dolphcode.tasktitans.database.tasks.Task;
import xyz.dolphcode.tasktitans.database.tasks.TaskGroup;
import xyz.dolphcode.tasktitans.util.OneScrollableAreaActivity;
import xyz.dolphcode.tasktitans.util.Util;

public class GroupTaskActivity extends OneScrollableAreaActivity implements DatabaseObserver {

    String id;
    User user;
    ArrayList<TaskGroup> groups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Client.addObserver(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_task);

        Util.setupConnectionChangedHandler(GroupTaskActivity.this);

        id = getIntent().getStringExtra("ID");
        user = Client.getUser(id);

        Button taskBackBtn = findViewById(R.id.groupBackBtn);
        Button createGroupBtn = findViewById(R.id.createGroupBtn);
        Button joinGroupBtn = findViewById(R.id.groupJoinBtn);
        Button openGroupBtn = findViewById(R.id.checkGroupBtn);
        Button backBtn = findViewById(R.id.groupBackBtn);

        ImageButton groupLeft = findViewById(R.id.groupLeftBtn);
        ImageButton groupRight = findViewById(R.id.groupRightBtn);

        displayText = findViewById(R.id.groupText);

        groups = Client.getTaskGroupsByMember(id);
        iterable = new ArrayList<String>();
        for (TaskGroup g:groups) {
            iterable.add(g.getGroupName());
        }

        checkSelection();
        displayText();

        openGroupBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (groups.size() > 0) {
                    Intent intent = new Intent(GroupTaskActivity.this, TaskGroupPageActivity.class);
                    intent.putExtra("ID", id);
                    intent.putExtra("GROUPID", groups.get(selection).getTaskGroupID());
                    GroupTaskActivity.this.startActivity(intent);
                }
            }
        });

        Util.addSwitchWithUser(backBtn, TasksActivity.class, GroupTaskActivity.this, id);

        groupLeft.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                selectionLeft();
            }
        });

        groupRight.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                selectionRight();
            }
        });

        joinGroupBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String code = ((EditText) findViewById(R.id.joinGroupInputTxt)).getText().toString();
                if (!code.isEmpty() && code.length() == 9) {
                    Intent intent = new Intent(GroupTaskActivity.this, TaskGroupPageActivity.class);
                    intent.putExtra("ID", id);

                    TaskGroup group = Client.getTaskGroupByJoinCode(code);
                    if (group != null)
                        group.addMember(user);

                    intent.putExtra("GROUPID", group.getTaskGroupID());

                    GroupTaskActivity.this.startActivity(intent);
                }
            }
        });

        createGroupBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String name = ((EditText) findViewById(R.id.createGroupNameTxt)).getText().toString();
                if (!name.isEmpty()) {
                    Intent intent = new Intent(GroupTaskActivity.this, TaskGroupPageActivity.class);
                    intent.putExtra("ID", id);

                    TaskGroup group = TaskGroup.TaskGroupBuilder.createTaskGroup(id, name)
                            .build();
                    Client.updateTaskGroup(group);
                    intent.putExtra("GROUPID", group.getTaskGroupID());

                    GroupTaskActivity.this.startActivity(intent);
                }
            }
        });

        Util.addSwitchWithUser(taskBackBtn, TasksActivity.class, GroupTaskActivity.this, getIntent().getStringExtra("ID"));
    }



    @Override
    public void databaseChanged() {
        groups = Client.getTaskGroupsByMember(id);
        iterable = new ArrayList<String>();
        for (TaskGroup g:groups) {
            iterable.add(g.getGroupName());
        }
        checkSelection();
        displayText();
    }
}
