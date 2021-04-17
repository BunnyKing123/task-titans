package xyz.dolphcode.tasktitans.database.tasks;

import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import xyz.dolphcode.tasktitans.database.Client;
import xyz.dolphcode.tasktitans.database.User;
import xyz.dolphcode.tasktitans.util.Util;

public final class TaskGroup {

    private String groupMembers;
    private String groupName;
    private String taskList;
    private String joinCode;
    private String taskGroupID;
    private String groupLeaderID;

    private HashMap<String, ArrayList<String>> taskListUsable;

    private TaskGroup() {}

    public String getTaskList() { return taskList; }
    public String getGroupName() { return groupName; }
    public String getGroupMembers() { return groupMembers; }
    public String getJoinCode() { return joinCode; }
    public String getGroupLeaderID() { return groupLeaderID; }
    public String getTaskGroupID() { return taskGroupID; }

    public HashMap<String, GroupMember> membersToMap() {
        HashMap<String, GroupMember> groupMembers = new HashMap<String, GroupMember>();

        String[] membersWithTasks = taskList.split(";");
        for (String str:membersWithTasks) {
            String[] memberData = str.split("-");
            ArrayList<String> tasks = new ArrayList<String>();
            for (String id:memberData[1].split(",")) {
                tasks.add(id);
            }
            GroupMember member = new GroupMember(memberData[0], tasks);
            groupMembers.put(memberData[0], member);
        }

        return groupMembers;
    }

    public void addMember(User user) {
        groupMembers += "-" + user.getID();
        taskList += ";" + user.getID() + "-none";
        Client.updateTaskGroup(this);
    }

    public void removeMember(User user) {
        taskListUsable.remove(user.getID());
        String updatedTaskList = "";
        String updatedMemberList = "";
        for (String key:taskListUsable.keySet()) {
            updatedTaskList = updatedTaskList + key + "-";
            updatedMemberList = updatedMemberList + key + "-";
            ArrayList<String> taskIDs = taskListUsable.get(key);
            String ids = Util.joinList(taskIDs, ",");
            updatedTaskList = updatedTaskList + ids + ";";
        }
        taskList = updatedTaskList.substring(0, updatedTaskList.length() - 1);
        groupMembers = updatedMemberList.substring(0, updatedMemberList.length() - 1);
        Client.updateTaskGroup(this);
    }

    public void removeTask(String userID, String taskID) {
        taskListUsable.get(userID).remove(taskID);
        String updatedTaskList = "";
        for (String key:taskListUsable.keySet()) {
            updatedTaskList = updatedTaskList + key + "-";
            ArrayList<String> taskIDs = taskListUsable.get(key);
            String ids = Util.joinList(taskIDs, ",");
            if (ids.isEmpty())
                ids = "none";
            updatedTaskList = updatedTaskList + ids + ";";
        }
        taskList = updatedTaskList.substring(0, updatedTaskList.length() - 1);
        Client.updateTaskGroup(this);
    }

    public void addTask(String userID, String taskID) {
        taskListUsable.get(userID).add(taskID);
        String updatedTaskList = "";
        for (String key:taskListUsable.keySet()) {
            updatedTaskList = updatedTaskList + key + "-";
            ArrayList<String> taskIDs = taskListUsable.get(key);
            String ids = Util.joinList(taskIDs, ",");
            updatedTaskList = updatedTaskList + ids + ";";
        }
        taskList = updatedTaskList.substring(0, updatedTaskList.length() - 1);
        Client.updateTaskGroup(this);
    }

    public static class TaskGroupBuilder {

        private String groupMembers = "";
        private String groupName;
        private String taskList;
        private String joinCode = "";
        private String taskGroupID;
        private String groupLeaderID;

        public static TaskGroupBuilder createTaskGroup(String leaderID, String groupName) {
            TaskGroupBuilder builder = new TaskGroupBuilder();
            builder.groupLeaderID = leaderID;
            builder.groupName = groupName;
            return builder;
        }

        // Used when client class creates Task Groups
        public TaskGroupBuilder setAll(String joinCode, String taskList, String groupMembers) {
            this.joinCode = joinCode;
            this.taskList = taskList;
            this.groupMembers = groupMembers;
            return this;
        }

        public TaskGroup build(String... args) {
            TaskGroup taskGroup = new TaskGroup();

            String[] data = null;

            if (args.length == 0 || this.joinCode.isEmpty())
                data = Client.getUniqueTaskGroupData();

            if (args.length > 0) {
                this.taskGroupID = args[0];
            } else {
                this.taskGroupID = data[0];
            }

            if (this.joinCode.isEmpty())
                this.joinCode = data[1];

            if (this.groupMembers.isEmpty()) {
                this.groupMembers = this.groupLeaderID;
                this.taskList = this.groupLeaderID + "-none";
            }

            taskGroup.taskGroupID = this.taskGroupID;
            taskGroup.joinCode = this.joinCode;
            taskGroup.groupLeaderID = this.groupLeaderID;
            taskGroup.groupMembers = this.groupMembers;
            taskGroup.taskList = this.taskList;
            taskGroup.groupName = this.groupName;

            taskGroup.taskListUsable = new HashMap<String, ArrayList<String>>();
            String[] members = this.taskList.split(";");
            for (String m:members) {
                String[] mSplit = m.split("-");
                GroupMember member = new GroupMember(mSplit[0], mSplit[1]);
                taskGroup.taskListUsable.put(member.getMemberID(), member.getTaskIDs());
            }

            return taskGroup;
        }
    }

}
