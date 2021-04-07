package xyz.dolphcode.tasktitans.database;

import java.util.ArrayList;
import java.util.List;

import xyz.dolphcode.tasktitans.util.Util;

public class Guild {

    private String name;
    private String guildID;
    private String dbChat;
    private String dbMemberIDs;
    private String ownerID = "";
    private int minimumLevel = 0;

    private List<String> chat;
    private List<String> memberIDs;

    public String getGuildName() { return name; }
    public String getGuildID() { return guildID; }
    public String getDBChat() { return dbChat; } // Required for storing in DB, can also be used to return text version of chat
    public String getDBMemberIDs() { return dbMemberIDs; } // Required for storing in DB
    public String getOwnerID() { return ownerID; }
    public int getMinimumLevel() { return minimumLevel; }

    public List<String> getChat() { return chat; }
    public List<String> getMemberIDs() { return memberIDs; }

    public void addMember(String id) {
        memberIDs.add(id);
        dbMemberIDs = dbMemberIDs + "|" + id;
    }

    public void chatMessage(String message, User user) {
        chat.add(user.getDisplayName() + ": " + message);
        dbChat = dbChat + "\n" + user.getDisplayName() + ": " + message;
        Client.updateGuild(this);
    }

    // Private constructor so that guild can't be instantiated
    private Guild() {}

    public static class GuildBuilder {

        private String name; // Guild name used for searching
        private List<String> chat = new ArrayList<String>();
        private List<String> memberIDs = new ArrayList<String>();
        private String ownerID;
        private int minimumLevel = 0;

        // Private constructor so that guild can't be instantiated
        private GuildBuilder() {}

        public static GuildBuilder createGuild(String name, String ownerID) {
            GuildBuilder builder = new GuildBuilder();
            builder.name = name;
            builder.ownerID = ownerID;
            return builder;
        }

        public GuildBuilder setMinimumLevel(int level) {
            if (level < 0) {
                this.minimumLevel = 0;
            } else if (level > 100) {
                this.minimumLevel = 100;
            } else {
                this.minimumLevel = level;
            }
            return this;
        }

        public GuildBuilder setChat(List<String> chat) {
            this.chat = chat;
            while (chat.size() > 100) {
                chat.remove(0);
            }
            return this;
        }

        public GuildBuilder setChat(String chat) {
            for (String str:chat.split("\n")) {
                this.chat.add(str);
            }
            while (this.chat.size() > 100) {
                this.chat.remove(0);
            }
            return this;
        }

        public GuildBuilder setMemberIDs(List<String> memberIDs) {
            this.memberIDs = memberIDs;
            return this;
        }

        public Guild build(String... args) {
            Guild guild = new Guild();

            guild.ownerID = this.ownerID;
            guild.chat = this.chat;
            guild.memberIDs = this.memberIDs;
            guild.minimumLevel = this.minimumLevel;
            guild.name = this.name;
            guild.guildID = (args.length > 0) ? args[0] : Client.getUniqueGuildID();
            guild.memberIDs.add(guild.ownerID);

            guild.dbChat = Util.joinList(guild.chat, "\n");
            guild.dbMemberIDs = Util.joinList(guild.memberIDs, "\n");

            return guild;
        }
    }

}
