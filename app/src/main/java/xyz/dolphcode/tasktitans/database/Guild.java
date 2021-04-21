package xyz.dolphcode.tasktitans.database;

import java.util.ArrayList;
import java.util.List;

import xyz.dolphcode.tasktitans.util.Util;

public class Guild {

    public static final int BOSS = 0;
    public static final int TASK = 1;

    private String name; // Guild name used to identify guild
    private String guildID; // Guild id also used to identify guild
    private String dbChat; // The DB Chat property is what I store in the database and extract from the database
    private String dbMemberIDs; // Same thing as DB Chat but for the list of members
    private String ownerID = ""; // Used to represent and identify the owner of a guild
    private int minimumLevel = 0; // Minimum level requirement to join a guild
    private int guildPoints = 0;
    private String guildTaskName;
    private String guildTaskType;
    private String guildTaskCompletions;
    private String guildTaskDeadline;

    private List<String> chat; // DB chat is converted into a list of strings in the chat
    private List<String> memberIDs; // DB member list is converted into a list of strings in the chat

    public String getGuildName() { return name; }
    public String getGuildID() { return guildID; }
    public String getDBChat() { return dbChat; } // Used for storing in the database, can also be used to return text version of chat
    public String getDBMemberIDs() { return dbMemberIDs; } // Used for storing in the database
    public String getOwnerID() { return ownerID; }
    public int getMinimumLevel() { return minimumLevel; }

    public List<String> getChat() { return chat; }
    public List<String> getMemberIDs() { return memberIDs; }

    // Adds a member to the list
    public void addMember(String id) {
        memberIDs.add(id); // Add to member list
        dbMemberIDs = dbMemberIDs + "-" + id; // Add member to string instead of converting member list to text
        Client.updateGuild(this); // Update guild in database
        Client.getUser(id).setGuild(this.guildID);
    }

    // Sends a message to the chat
    public void chatMessage(String message, User user) {
        chat.add(user.getDisplayName() + ": " + message); // Update chat in list form
        dbChat = dbChat + "\n" + user.getDisplayName() + ": " + message; // Update chat in text form instead of converting chat from list to text
        Client.updateGuild(this); // Update guild in database
    }

    // Private constructor so that guild can't be instantiated
    private Guild() {}

    public static class GuildBuilder {

        private String name;
        private List<String> chat = new ArrayList<String>();
        private List<String> memberIDs = new ArrayList<String>();
        private String ownerID;
        private int minimumLevel = 0;

        // Private constructor so that guild can't be instantiated
        private GuildBuilder() {}

        // Returns a builder class used to set up a Guild object
        public static GuildBuilder createGuild(String name, String ownerID) { // Guild name and owner ID are required values and must be provided when calling createGuild
            GuildBuilder builder = new GuildBuilder();
            builder.name = name;
            builder.ownerID = ownerID;
            return builder;
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
        public GuildBuilder setChat(List<String> chat) {
            this.chat = chat;
            while (chat.size() > 100) {
                chat.remove(0);
            }
            return this;
        }

        // Set the chat of the guild given the whole chat in string format
        public GuildBuilder setChat(String chat) {
            for (String str:chat.split("\n")) {
                this.chat.add(str);
            }
            while (this.chat.size() > 100) {
                this.chat.remove(0);
            }
            return this;
        }

        // Set the member list of the guild
        public GuildBuilder setMemberIDs(List<String> memberIDs) {
            this.memberIDs = memberIDs;
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

            return guild;
        }
    }

}
