package xyz.dolphcode.tasktitans.database;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import xyz.dolphcode.tasktitans.database.tasks.Task;
import xyz.dolphcode.tasktitans.database.tasks.TaskGroup;
import xyz.dolphcode.tasktitans.resources.TaskType;
import xyz.dolphcode.tasktitans.util.Util;

// The client class deals with interactions with the database
public final class Client {

    private static DatabaseReference INSTANCE;
    private static ArrayList<String> USERIDS = null;
    private static ArrayList<User> USERS = null;
    private static ArrayList<String> GUILDIDS = null;
    private static ArrayList<Guild> GUILDS = null;
    private static ArrayList<String> TASKIDS = null;
    private static ArrayList<Task> TASKS = null;
    private static ArrayList<String> TASKGROUPIDS = null;
    private static ArrayList<String> TASKGROUPCODES = null;
    private static ArrayList<TaskGroup> TASKGROUPS = null;

    private static ArrayList<DatabaseObserver> OBSERVERS = new ArrayList<DatabaseObserver>();

    static {
        INSTANCE = FirebaseDatabase.getInstance().getReference();
    }

    // Private constructor
    private Client() {}

    //
    public static void init() {
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) { // According to Firebase docs, called once ValueEventListener is added to DB instance
                ArrayList<String> userIDs = new ArrayList<String>();
                ArrayList<User> users = new ArrayList<User>();
                ArrayList<String> guildIDs = new ArrayList<String>();
                ArrayList<Guild> guilds = new ArrayList<Guild>();
                ArrayList<String> taskIDs = new ArrayList<String>();
                ArrayList<Task> tasks = new ArrayList<Task>();
                ArrayList<String> taskGroupIDs = new ArrayList<String>();
                ArrayList<String> taskGroupJoinCodes = new ArrayList<String>();
                ArrayList<TaskGroup> taskGroups = new ArrayList<TaskGroup>();

                // Iterate through all entries in "users"
                for (DataSnapshot child : dataSnapshot.child("users").getChildren()) {
                    userIDs.add(child.getKey()); // Add the ID found to the list of User IDs

                    // Use the UserBuilder to create a user using the information from the Data Snapshot
                    User user = User.UserBuilder.createUser(child.child("username").getValue().toString(), child.child("password").getValue().toString())
                            .setDisplayName(child.child("displayName").getValue().toString())
                            .setEmail(child.child("email").getValue().toString())
                            .setGuildID(child.child("guildID").getValue().toString())
                            .setColor(((Long) child.child("colorID").getValue()).intValue(), false)
                            .setColor(((Long) child.child("hairColorID").getValue()).intValue(), true)
                            .setInventory(child.child("inventory").getValue().toString())
                            .setEquipment(child.child("equipment").getValue().toString(), child.child("pet").getValue().toString())
                            .setSpecialty(((Long) child.child("raceID").getValue()).intValue(), true)
                            .setSpecialty(((Long) child.child("classID").getValue()).intValue(), false)
                            .setModifications(
                                    ((Long) child.child("constMod").getValue()).intValue(),
                                    ((Long) child.child("strengthMod").getValue()).intValue(),
                                    ((Long) child.child("intelMod").getValue()).intValue(),
                                    ((Long) child.child("dextMod").getValue()).intValue()
                            )
                            .setMoney(((Long) child.child("money").getValue()).doubleValue())
                            .setMana(((Long) child.child("mana").getValue()).doubleValue())
                            .setCurrentHP(((Long) child.child("hp").getValue()).intValue())
                            .setXP(((Long) child.child("xp").getValue()).intValue())
                            .build(child.getKey());
                    users.add(user);
                }

                // Iterate through all entries in "guilds"
                for (DataSnapshot child : dataSnapshot.child("guilds").getChildren()) {
                    guildIDs.add(child.getKey()); // Add the ID found to the list of Guild IDs

                    // Use the GuildBuilder to create a guild using the information from the Data Snapshot
                    Guild guild = Guild.GuildBuilder.createGuild(child.child("guildName").getValue().toString(), child.child("ownerID").getValue().toString())
                            .setChat(child.child("dbchat").getValue().toString())
                            .setMemberIDs(Util.toList(child.child("dbmemberIDs").getValue().toString(), "|"))
                            .setMinimumLevel(((Long) child.child("minimumLevel").getValue()).intValue())
                            .build(child.getKey());
                    guilds.add(guild);
                }

                // Iterate through all entries in "tasks"
                for (DataSnapshot child : dataSnapshot.child("tasks").getChildren()) {
                    taskIDs.add(child.getKey()); // Add the ID found to the list of Task IDs

                    // Use the TaskBuilder to create a task using the information from the Data Snapshot
                    Task task;
                    if (((Long) child.child("taskType").getValue()).intValue() == TaskType.TASK) {
                        task = Task.TaskBuilder.createTask(child.child("taskOwnerID").getValue().toString(), child.child("taskName").getValue().toString(), child.child("deadline").getValue().toString())
                                .setCount(((Long) child.child("taskCount").getValue()).intValue())
                                .setDesc(child.child("taskDesc").getValue().toString())
                                .build(child.getKey());
                    } else {
                        task = Task.TaskBuilder.createRepeatTask(child.child("taskOwnerID").getValue().toString(), child.child("taskName").getValue().toString(),
                                    child.child("freqData").getValue().toString(), ((Long) child.child("freqType").getValue()).intValue())
                                .setCount(((Long) child.child("taskCount").getValue()).intValue())
                                .setDesc(child.child("taskDesc").getValue().toString())
                                .setLastFinished(child.child("lastFinished").getValue().toString())
                                .build(child.getKey());
                    }
                    tasks.add(task);
                }

                // Iterate through all entries in "taskgroups"
                for (DataSnapshot child : dataSnapshot.child("taskgroups").getChildren()) {
                    taskGroupIDs.add(child.getKey()); // Add the ID found to the list of Task Group IDs
                    taskGroupJoinCodes.add(child.child("joinCode").getValue().toString());

                    // Use the TaskGroupBuilder to create a task group using the information from the Data Snapshot
                    TaskGroup taskGroup = TaskGroup.TaskGroupBuilder.createTaskGroup(child.child("groupLeaderID").getValue().toString(), child.child("groupName").getValue().toString())
                            .setAll(child.child("joinCode").getValue().toString(),
                                    child.child("taskList").getValue().toString(),
                                    child.child("groupMembers").getValue().toString())
                            .build(child.getKey());
                    taskGroups.add(taskGroup);
                }

                // Set static variables
                USERIDS = userIDs;
                USERS = users;
                GUILDIDS = guildIDs;
                GUILDS = guilds;
                TASKIDS = taskIDs;
                TASKS = tasks;
                TASKGROUPIDS = taskGroupIDs;
                TASKGROUPCODES = taskGroupJoinCodes;
                TASKGROUPS = taskGroups;

                for (DatabaseObserver o:OBSERVERS) {
                    o.databaseChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {} // Required according to docs
        };

        INSTANCE.addValueEventListener(listener); // Add the event listener to the instance of the database
    }

    // Adds an observer to be notified when the database has changed
    public static void addObserver(DatabaseObserver observer) { OBSERVERS.add(observer); }

    // Removes an observer from the list of observers being notified about changes in the database
    public static void removeObserver(DatabaseObserver observer) { OBSERVERS.remove(observer); }

    // Returns a User with a particular username if the password is correct
    // Return null if a match is not found or password is incorrect
    public static User getUser(String username, String password) {
        for (User user : USERS) { // Iterate through all Users
            if (user.getUsername().contentEquals(username)) { // If a match is found
                // If the password is incorrect return null
                if (!user.getPassword().contentEquals(password))
                    return null;

                return user; // Return that user
            }
        }
        return null; // Return null
    }

    // Returns a User with a certain ID
    // Returns null if none are found
    public static User getUser(String id) {
        for (User user : USERS) { // Iterate through all Users
            if (user.getID().contentEquals(id)) { // If a match is found
                return user;
            }
        }
        return null; // Return null
    }

    // Updates a User in the database
    public static void updateUser(User user) {
        INSTANCE.child("users").child(user.getID()).setValue(user);
    }

    // Returns a Guild with a certain ID
    // Returns null if none are found
    public static Guild getGuild(String id) {
        for (Guild guild : GUILDS) { // Iterate through all Users
            if (guild.getGuildID().contentEquals(id)) { // If a match is found
                return guild;
            }
        }
        return null; // Return null
    }

    // Returns a list of all guilds that contain the search query
    // Used to search for guilds in the guild search screen
    public static ArrayList<Guild> searchGuilds(String search) {
        String searchLower = search.toLowerCase();
        ArrayList<Guild> guildList = new ArrayList<Guild>();
        for (Guild guild : GUILDS) { // Iterate through all Users
            Log.v("TEST", guild.getGuildName() + " " + guild.getGuildName().toLowerCase().contains(searchLower));
            if (guild.getGuildName().toLowerCase().contains(searchLower)) { // If a match is found
                guildList.add(guild);
            }
        }
        return guildList; // Return null
    }

    // Updates a User in the database
    public static void updateGuild(Guild guild) {
        INSTANCE.child("guilds").child(guild.getGuildID()).setValue(guild);
    }

    // Returns a list of all the tasks that a user has
    public static ArrayList<Task> getTasksByUser(String id) {
        ArrayList<Task> filtered = new ArrayList<Task>();
        for (Task task: TASKS) {
            if (task.getTaskOwnerID().contentEquals(id)) {
                filtered.add(task);
            }
        }
        return filtered;
    }

    // Returns a User with a certain ID
    // Returns null if none are found
    public static Task getTask(String id) {
        for (Task task : TASKS) { // Iterate through all Users
            if (task.getTaskID().contentEquals(id)) { // If a match is found
                return task;
            }
        }
        return null; // Return null
    }

    // Updates a User in the database
    public static void updateTask(Task task) {
        INSTANCE.child("tasks").child(task.getTaskID()).setValue(task);
    }

    // Removes a task from the database
    public static void removeTask(Task task) {
        INSTANCE.child("tasks").child(task.getTaskID()).removeValue();
    }

    // Returns a list of all the tasks that a user has
    public static TaskGroup getTaskGroupByJoinCode(String code) {
        for (TaskGroup group: TASKGROUPS) {

            if (group.getJoinCode().contentEquals(code)) {
                return group;
            }
        }
        return null;
    }

    // Returns an arraylist of all task groups that includes a particular member with the provided member id
    public static ArrayList<TaskGroup> getTaskGroupsByMember(String member) {
        ArrayList<TaskGroup> groups = new ArrayList<TaskGroup>();
        for (TaskGroup group: TASKGROUPS) {
            for (String m:group.getGroupMembers().split("-")) {
                if (m.contentEquals(member)) {
                    groups.add(group);
                    break;
                }
            }
        }
        return groups;
    }

    // Returns a task group with a matching id
    public static TaskGroup getTaskGroup(String id) {
        for (TaskGroup taskGroup : TASKGROUPS) { // Iterate through all Users
            if (taskGroup.getTaskGroupID().contentEquals(id)) { // If a match is found
                return taskGroup;
            }
        }
        return null; // Return null
    }

    // Updates a TaskGroup in the database
    public static void updateTaskGroup(TaskGroup taskGroup) {
        INSTANCE.child("taskgroups").child(taskGroup.getTaskGroupID()).setValue(taskGroup);
    }

    // Creates a Unique User ID
    public static String getUniqueUserID() {
        String uuid = Util.generateRandomString(16);
        while (USERIDS.contains(uuid)) {
            uuid = Util.generateRandomString(16);
        }
        return uuid;
    }

    // Creates a Unique Guild ID
    public static String getUniqueGuildID() {
        String uuid = Util.generateRandomString(16);
        while (GUILDIDS.contains(uuid)) {
            uuid = Util.generateRandomString(16);
        }
        return uuid;
    }

    // Creates a Unique Task ID
    public static String getUniqueTaskID() {
        String uuid = Util.generateRandomString(16);
        while (TASKIDS.contains(uuid)) {
            uuid = Util.generateRandomString(16);
        }
        return uuid;
    }

    // Generates a Unique Task Group ID and Join Code
    public static String[] getUniqueTaskGroupData() {
        String uuid = Util.generateRandomString(16);
        while (TASKGROUPIDS.contains(uuid)) {
            uuid = Util.generateRandomString(16);
        }

        String joinCode = Util.generateRandomNumString(3) + Util.generateRandomString(6);
        while (TASKGROUPCODES.contains(joinCode)) {
            joinCode = Util.generateRandomNumString(3) + Util.generateRandomString(6);
        }

        String[] data = {uuid, joinCode};
        return data;
    }

}
