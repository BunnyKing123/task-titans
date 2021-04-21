package xyz.dolphcode.tasktitans;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class TaskGroupPanelActivity extends OneScrollableAreaActivity implements DatabaseObserver {

    String id;
    String groupID;
    TaskGroup group;
    Intent prev;
    ArrayList<String> userIDs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_group_panel);

        Util.setupConnectionChangedHandler(TaskGroupPanelActivity.this);

        Client.addObserver(this);

        prev = getIntent();
        id = prev.getStringExtra("ID");
        groupID = prev.getStringExtra("GROUPID");
        group = Client.getTaskGroup(groupID);

        userIDs = new ArrayList<String>();
        iterable = new ArrayList<String>();
        for (String str:group.getGroupMembers().split("-")) {
            userIDs.add(str);
            User user = Client.getUser(str);
            iterable.add(user.getUsername() + "\nA.K.A. " + user.getDisplayName());
        }

        ImageButton leftBtn = findViewById(R.id.groupMemberLeftButton);
        ImageButton rightBtn = findViewById(R.id.groupMemberRightButton);
        displayText = findViewById(R.id.groupMemberText);
        defaultText = "This group has no members";

        Button assignBtn = findViewById(R.id.assignTaskBtn);
        Button removeBtn = findViewById(R.id.removeMemberBtn);
        Button backBtn = findViewById(R.id.groupPanelBackBtn);

        checkSelection();
        displayText();

        removeBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                User removing = Client.getUser(userIDs.get(selection));
                if (!removing.getID().contentEquals(id))
                    group.removeMember(removing);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(TaskGroupPanelActivity.this, TaskGroupPageActivity.class);
                intent.putExtra("ID", id);
                intent.putExtra("GROUPID", groupID);
                TaskGroupPanelActivity.this.startActivity(intent);
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

        assignBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(TaskGroupPanelActivity.this, TaskCreationActivity.class);
                intent.putExtra("ID", id);
                intent.putExtra("TARGETID", userIDs.get(selection));
                intent.putExtra("GROUPID", groupID);
                TaskGroupPanelActivity.this.startActivity(intent);
            }
        });
    }

    @Override
    public void databaseChanged() {
        group = Client.getTaskGroup(groupID);
        if (group == null) {
            Intent intent = new Intent(TaskGroupPanelActivity.this, GroupTaskActivity.class);
            intent.putExtra("ID", id);
            TaskGroupPanelActivity.this.startActivity(intent);
        }
        userIDs = new ArrayList<String>();
        iterable = new ArrayList<String>();
        for (String str:group.getGroupMembers().split("-")) {
            userIDs.add(str);
            User user = Client.getUser(str);
            iterable.add(user.getUsername() + "\nA.K.A. " + user.getDisplayName());
        }
        checkSelection();
        displayText();
    }
}
