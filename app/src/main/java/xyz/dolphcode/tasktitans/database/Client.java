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
                            .setSpecialty(((Long) child.child("raceID").getValue()).intValue(), true)
                            .setSpecialty(((Long) child.child("classID").getValue()).intValue(), false)
                            .setMoney(((Long) child.child("money").getValue()).doubleValue())
                            .setMana(((Long) child.child("mana").getValue()).doubleValue())
                            .setCurrentHP(((Long) child.child("hp").getValue()).intValue())
                            .setXP(((Long) child.child("xp").getValue()).intValue())
                            .build(child.getKey());
                    users.add(user);
                }

                // Iterate through all entries in "guilds"
                for (DataSnapshot child : dataSnapshot.child("guilds").getChildren()) {
                    guildIDs.add(child.getKey()); // Add the ID found to the list of User IDs

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
                    taskIDs.add(child.getKey()); // Add the ID found to the list of User IDs

                    // Use the Task to create a task using the information from the Data Snapsho
                    Task task = Task.TaskBuilder.createTask(child.child("taskOwnerID").toString(), child.child("taskName").toString(), child.child("deadline").toString())
                            .setCount(((Long) child.child("taskCount").getValue()).intValue())
                            .setDesc(child.child("taskDesc").toString())
                            .build(child.getKey());
                    tasks.add(task);
                }

                // Set static variables
                USERIDS = userIDs;
                USERS = users;
                GUILDIDS = guildIDs;
                GUILDS = guilds;
                TASKIDS = taskIDs;
                TASKS = tasks;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {} // Required according to docs
        };

        INSTANCE.addValueEventListener(listener); // Add the event listener to the instance of the database
    }

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

    // Updates a User in the database
    public static void updateGuild(Guild guild) {
        INSTANCE.child("guilds").child(guild.getGuildID()).setValue(guild);
    }

    // Updates a User in the database
    public static void updateTask(Task task) {
        INSTANCE.child("tasks").child(task.getTaskID()).setValue(task);
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

}
