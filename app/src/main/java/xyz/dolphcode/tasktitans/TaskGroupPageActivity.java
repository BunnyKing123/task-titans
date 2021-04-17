package xyz.dolphcode.tasktitans;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;

import xyz.dolphcode.tasktitans.database.Client;
import xyz.dolphcode.tasktitans.database.DatabaseObserver;
import xyz.dolphcode.tasktitans.database.User;
import xyz.dolphcode.tasktitans.database.tasks.GroupMember;
import xyz.dolphcode.tasktitans.database.tasks.Task;
import xyz.dolphcode.tasktitans.database.tasks.TaskGroup;
import xyz.dolphcode.tasktitans.util.OneScrollableAreaActivity;
import xyz.dolphcode.tasktitans.util.Util;

public class TaskGroupPageActivity extends OneScrollableAreaActivity implements DatabaseObserver {

    String id;
    String groupID;
    TaskGroup group;
    Intent prev;
    ArrayList<String> taskIDs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_group_page);

        Client.addObserver(this);

        prev = getIntent();
        id = prev.getStringExtra("ID");
        groupID = prev.getStringExtra("GROUPID");
        group = Client.getTaskGroup(groupID);

        GroupMember member = group.membersToMap().get(id);
        for (String str:group.membersToMap().keySet()) {
        }
        taskIDs = member.getTaskIDs();
        iterable = new ArrayList<String>();
        for (String taskID:taskIDs) {
            if(taskID.equals("none"))
                break;
            Task t = Client.getTask(taskID);
            iterable.add(t.getTaskName() + "\nDesc: " + t.getTaskDesc() + "\nDue: " + t.getDeadline());
        }

        ImageButton leftBtn = findViewById(R.id.groupTaskLeftBtn);
        ImageButton rightBtn = findViewById(R.id.groupTaskRightBtn);
        displayText = findViewById(R.id.groupTaskTxt);
        defaultText = "No group tasks available. Check with you group task leader";

        checkSelection();
        displayText();

        Button panelBtn = findViewById(R.id.openGroupPanelBtn);
        Button leaveBtn = findViewById(R.id.leaveGroupBtn);
        Button backBtn = findViewById(R.id.groupPageBackBtn);
        Button completeBtn = findViewById(R.id.completeGroupTaskBtn);

        Util.addSwitchWithUser(backBtn, GroupTaskActivity.class, TaskGroupPageActivity.this, id);

        leaveBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                User removing = Client.getUser(id);
                if (!group.getGroupLeaderID().contentEquals(id))
                    group.removeMember(removing);
            }
        });

        completeBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Task removingTask = Client.getTask(taskIDs.get(selection));
                if (removingTask.getTaskCount() == 1) {
                    group.removeTask(id, removingTask.getTaskID());
                }
                removingTask.finish();
            }
        });

        leftBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                selectionLeft();
            }
        });

        rightBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                selectionRight();
            }
        });

        panelBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (group.getGroupLeaderID().contentEquals(id)) {
                    Intent intent = new Intent(TaskGroupPageActivity.this, TaskGroupPanelActivity.class);
                    Util.transferData(prev, intent);
                    TaskGroupPageActivity.this.startActivity(intent);
                }
            }
        });
    }

    @Override
    public void databaseChanged() {
        group = Client.getTaskGroup(groupID);
        if (group == null) {
            Intent intent = new Intent(TaskGroupPageActivity.this, GroupTaskActivity.class);
            intent.putExtra("ID", id);
            TaskGroupPageActivity.this.startActivity(intent);
        }
        GroupMember member = group.membersToMap().get(id);
        if (member == null) {
            Intent intent = new Intent(TaskGroupPageActivity.this, GroupTaskActivity.class);
            intent.putExtra("ID", id);
            TaskGroupPageActivity.this.startActivity(intent);
        }
        for (String str:group.membersToMap().keySet()) {
        }
        taskIDs = member.getTaskIDs();
        iterable = new ArrayList<String>();
        for (String taskID:taskIDs) {
            if(taskID.equals("none"))
                break;
            Task t = Client.getTask(taskID);
            iterable.add(t.getTaskName() + "\nDesc: " + t.getTaskDesc() + "\nDue: " + t.getDeadline());
        }
        checkSelection();
        displayText();
    }
}
