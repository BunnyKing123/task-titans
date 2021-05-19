package xyz.dolphcode.tasktitans.database.guilds;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import xyz.dolphcode.tasktitans.database.Client;
import xyz.dolphcode.tasktitans.database.User;
import xyz.dolphcode.tasktitans.util.Util;

public class Guild {

    private String name; // Guild name used to identify guild
    private String guildID; // Guild id also used to identify guild
    private String dbChat; // The DB Chat property is what I store in the database and extract from the database
    private String dbMemberIDs; // Same thing as DB Chat but for the list of members
    private String ownerID = ""; // Used to represent and identify the owner of a guild
    private int minimumLevel = 0; // Minimum level requirement to join a guild
    private int guildPoints = 0;
    private String guildTaskName;
    private int guildTaskType;
    private int guildBossHP;
    private String guildTaskCompletions;
    private String guildTaskDeadline;

    private ArrayList<String> chat; // DB chat is converted into a list of strings in the chat
    private ArrayList<String> memberIDs; // DB member list is converted into a list of strings in the chat

    public String getGuildName() { return name; }
    public String getGuildID() { return guildID; }
    public String getDBChat() { return dbChat; } // Used for storing in the database, can also be used to return text version of chat
    public String getDBMemberIDs() { return dbMemberIDs; } // Used for storing in the database
    public String getOwnerID() { return ownerID; }
    public int getMinimumLevel() { return minimumLevel; }
    public int getGuildPoints() { return guildPoints; }
    public String getGuildTaskName() { return guildTaskName; }
    public int getGuildTaskType() { return guildTaskType; }
    public int getGuildBossHP() { return guildBossHP; }
    public String getGuildCompletions() { return guildTaskCompletions; }
    public String getGuildTaskDeadline() { return guildTaskDeadline; }

    public List<String> getChat() { return chat; }
    public List<String> getMemberIDs() { return memberIDs; }

    // Adds a member to the list
    public void addMember(String id) {
        memberIDs.add(id); // Add to member list
        dbMemberIDs = dbMemberIDs + "-" + id; // Add member to string instead of converting member list to text
        Log.v("ADD_MEMBER_GUILD", dbMemberIDs);
        Log.v("ADD_MEMBER_MEMBERCOUNT_GUILD", "" + memberIDs.size());
        Client.updateGuild(this); // Update guild in database
        Client.getUser(id).setGuild(this.guildID);
    }

    // Sends a message to the chat
    public void chatMessage(String message, User user) {
        chat.add(user.getDisplayName() + ": " + message); // Update chat in list form
        dbChat = dbChat + "\n" + user.getDisplayName() + ": " + message; // Update chat in text form instead of converting chat from list to text
        Log.v("CHAT_MSG_GUILD", dbChat);
        Log.v("MSG_COUNT_GUILD", "" + chat.size());
        Client.updateGuild(this); // Update guild in database
    }

    public void damageBoss(int damage) {
        if (guildTaskType == GuildTask.BOSS) {
            guildBossHP = (guildBossHP - damage < 0) ? 0 : guildBossHP - damage;
        }
        Client.updateGuild(this);
    }

    public void completeTask(User user) {
        guildTaskCompletions = guildTaskCompletions + user.getID() + "-";
        Client.updateGuild(this);
    }

    public void regenerateChallenge() {
        GuildTask guildTask = new GuildTask();
        this.guildTaskCompletions = "";
        this.guildTaskName = guildTask.getTaskName();
        this.guildTaskDeadline = guildTask.getDeadline();
        this.guildTaskType = guildTask.getTaskType();
        this.guildBossHP = (guildTaskType == GuildTask.BOSS) ? GuildTask.BOSSES.get(guildTaskName) : 0;
        Client.updateGuild(this);
    }

    // Private constructor so that guild can't be instantiated
    private Guild() {}

    public static class GuildBuilder {

        private String name;
        private ArrayList<String> chat = new ArrayList<String>();
        private ArrayList<String> memberIDs = new ArrayList<String>();
        private String ownerID;
        private int minimumLevel = 0;
        private int guildPoints = 0;
        private String guildTaskName = "none";
        private int guildTaskType = -1;
        private int guildBossHP = 0;
        private String guildTaskCompletions = "none";
        private String guildTaskDeadline = "none";

        // Private constructor so that guild can't be instantiated
        private GuildBuilder() {}

        // Returns a builder class used to set up a Guild object
        public static GuildBuilder createGuild(String name, String ownerID) { // Guild name and owner ID are required values and must be provided when calling createGuild
            GuildBuilder builder = new GuildBuilder();
            builder.name = name;
            builder.ownerID = ownerID;
            return builder;
        }

        public GuildBuilder setGuildPoints (int points) {
            this.guildPoints = points;
            return this;
        }

        public GuildBuilder setGuildTaskData(int type, String completions, String deadline) {
            this.guildTaskType = (type == GuildTask.BOSS || type == GuildTask.TASK) ? type : GuildTask.TASK;
            this.guildTaskCompletions = completions;
            this.guildTaskDeadline = deadline;
            return this;
        }

        public GuildBuilder setOtherGuildTaskData(String name, int hp) {
            this.guildTaskName = name;
            this.guildBossHP = hp;
            return this;
        }

        // Sets the minimum level requirement to join a guild
        public GuildBuilder setMinimumLevel(int level) {
            if (level < 0) {
                this.minimumLevel = 0;
            } else if (level > 100) { // Cap at 100 for balancing purposes
                this.minimumLevel = 100;
            } else {
                this.minimumLevel = level;
            }
            return this;
        }

        // Set the chat of the guild given a list of messages, also cuts chat down to the most recent 100 messages for storage conservation
        public GuildBuilder setChat(ArrayList<String> chat) {
            this.chat = chat;
            while (chat.size() > 100) {
                chat.remove(0);
            }
            return this;
        }

        // Set the chat of the guild given the whole chat in string format
        public GuildBuilder setChat(String chat) {
            String[] chatList = chat.split("\n");
            for (String str:chatList) {
                this.chat.add(str);
            }
            while (this.chat.size() > 100) {
                this.chat.remove(0);
            }
            return this;
        }

        // Set the member list of the guild
        public GuildBuilder setMemberIDs(ArrayList<String> memberIDs) {
            this.memberIDs = memberIDs;
            return this;
        }

        // Set the member list of the guild
        public GuildBuilder setMemberIDs(String memberIDs) {
            for (String str:memberIDs.split("\n")) {
                this.memberIDs.add(str);
            }
            while (this.memberIDs.size() > 100) {
                this.memberIDs.remove(0);
            }
            return this;
        }

        // Returns the finished Guild object
        public Guild build(String... args) {
            Guild guild = new Guild();

            guild.ownerID = this.ownerID;
            guild.chat = this.chat;
            guild.memberIDs = this.memberIDs;
            guild.minimumLevel = this.minimumLevel;
            guild.name = this.name;
            guild.guildID = (args.length > 0) ? args[0] : Client.getUniqueGuildID(); // Create a unique ID if one isn't already provided
            if (!guild.memberIDs.contains(guild.ownerID))
                guild.memberIDs.add(guild.ownerID); // Add the owner's ID if it isn't already in the member list

            guild.dbChat = Util.joinList(guild.chat, "\n"); // Set the dbChat property for uploading information to database
            guild.dbMemberIDs = Util.joinList(guild.memberIDs, "-"); // Same as line above but for member ids

            if (this.guildTaskType < 0) {
                GuildTask guildTask = new GuildTask();
                guild.guildTaskCompletions = "";
                guild.guildTaskName = guildTask.getTaskName();
                guild.guildTaskDeadline = guildTask.getDeadline();
                guild.guildTaskType = guildTask.getTaskType();
                guild.guildBossHP = (guildTask.getTaskType() == GuildTask.BOSS) ? GuildTask.BOSSES.get(guildTask.getTaskName()) : 0;
            } else {
                guild.guildTaskType = this.guildTaskType;
                guild.guildTaskName = this.guildTaskName;
                guild.guildTaskCompletions = this.guildTaskCompletions;
                guild.guildTaskDeadline = this.guildTaskDeadline;
                guild.guildBossHP = this.guildBossHP;
            }

            return guild;
        }
    }

}
