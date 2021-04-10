package xyz.dolphcode.tasktitans.database;

import android.util.Log;

import xyz.dolphcode.tasktitans.resources.Abilities;

public class User {

    private String username;
    private String password;
    private String displayName;
    private String email;
    private String id; // Used to access in database

    private int colorID = 0; // 4 possible skin colors: 0 - 3
    private int hairColorID = 0; // 4 possible hair colors: 0 - 3
    private int raceID = 0; // 3 possible races: 0 - 2
    private int classID = 0; // 3 possible classes: 0 - 2

    private int gender = 0; // 0 represents female, 1 represents male

    private double money = 0;
    private double mana = 0;
    private double maxMana = 0;

    private int xp = 0;
    private int hp = 100;
    private int maxHp = 100;

    private int baseStrength = 0;
    private int baseDext = 0;
    private int baseIntel = 0;
    private int baseConst = 0;

    private int strengthMod = 0;
    private int dextMod = 0;
    private int intelMod = 0;
    private int constMod = 0;

    private String skill = "";

    private String guildID = "";

    private User() {}

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public String getDisplayName() { return displayName; }
    public String getID() { return id; }

    public int getColorID() { return colorID; }
    public int getHairColorID() { return hairColorID; }
    public int getRaceID() { return raceID; }
    public int getClassID() { return classID; }
    public int getGender() { return gender; }

    public double getMoney() { return money; }
    public double getMana() { return mana; }
    public double getMaxMana() { return maxMana; }

    public int getXP() { return xp; }
    public int getHP() { return hp; }
    public int getMaxHP() { return maxHp; }

    public int getBaseStrength() { return baseStrength; }
    public int getBaseDext() { return baseDext; }
    public int getBaseIntel() { return baseIntel; }
    public int getBaseConst() { return baseConst; }

    public int getStrengthMod() { return strengthMod; }
    public int getDextMod() { return dextMod; }
    public int getIntelMod() { return intelMod; }
    public int getConstMod() { return constMod; }

    public String getSkill() { return skill; }

    public String getGuildID() { return guildID; }

    // Converts gender value to boolean
    public boolean isMale() { return gender == 1; }

    public void clearGuild() {
        this.guildID = "none";
    }

    public void setGuild(String guildID) {
        this.guildID = guildID;
        Client.updateUser(this);
    }

    // STATIC METHODS

    // Generates stats based on class and race
    // These stats are used to calculate other stats or data like HP and Mana
    public static int[] generateStats(int raceID, int classID) {
        int[] stats = {0, 0, 0, 0};

        if (raceID == 0) { // Human stats
            stats[0] = 2;
            stats[1] = 2;
            stats[2] = 2;
            stats[3] = 3;
        } else if (raceID == 1) { // Orc stats
            stats[0] = 3;
            stats[1] = 2;
            stats[2] = 1;
            stats[3] = 3;
        } else { // Elf stats
            stats[0] = 1;
            stats[1] = 3;
            stats[2] = 3;
            stats[3] = 2;
        }

        if (classID == 0) { // Warrior stats
            stats[0] += 2;
            stats[3] += 2;
        } else if (classID == 1) { // Archer stats
            stats[1] += 2;
            stats[2] += 2;
        } else { // Mage stats
            stats[2] += 2;
            stats[3] += 2;
        }
        return stats;
    }

    // Converts experience to the number of levels
    public static int convertToLevel(int xp) {
        int level = 1;
        while (xp > 0) { // Subtracts each amount of xp required to level up for each level until xp goes below 0 or becomes 0
            xp -= toNextLevel(level);
            if (xp >= 0)
                level++;
        }

        return level;
    }

    // Calculates amount of xp needed to level up
    // I used desmos to generate the graph of the function I was using for my calculations and adjusted them accordingly
    public static int toNextLevel(int currentLevel) {
        return (int) Math.floor(100 * Math.pow(2, 0.05 * (currentLevel - 1))); // Exponential Function
    }

    // Calculate the user's max HP given their constitution and level
    public static int calculateMaxHP(int level, int constitution) {
        double lvlDbl = (double) level;
        double constDbl = (double) constitution;
        return (int) Math.floor((120.0 * (1.0 / constDbl) * ((double) Math.log(lvlDbl/300.0))) + 367.6);
    }

    // Calculate the user's max Mana given their intelligence and level
    public static int calculateMaxMana(int level, int intelligence) {
        double lvlDbl = (double) level;
        double intelDbl = (double) intelligence;
        return (int) Math.floor((120.0 * (1.0 / intelDbl) * ((double) Math.log(lvlDbl/300.0))) + 367.6);
    }

    // Used for calculating user's max Mana and max HP
    public static int calculateStat(int level, int independentStat) {
        double lvlDbl = (double) level;
        double indepDbl = (double) independentStat;
        return (int) Math.floor((120.0 * (1.0 / indepDbl) * ((double) Math.log(lvlDbl/300.0))) + 367.6);
    }

    // Based on the Builder design pattern which I learned from HeadFirst Design Patterns
    public static class UserBuilder {

        // Private constructor so that the class cannot be instantiated
        private UserBuilder() {}

        // All the details for the user class
        private String username;
        private String password;
        private String displayName = null;
        private String email = null;
        private int colorID = 0;
        private int hairColorID = 0;
        private int raceID = 0;
        private int classID = 0;
        private int gender = 0;
        private double money = 0;
        private double mana = -1; // Mana in builder class is set to -1 by default because this is how we determine whether or not the mana was set with the builder object
        private int xp = 0;
        private int hp = -1; // Reasoning for default is same as mana

        private int strengthMod = 0;
        private int dextMod = 0;
        private int intelMod = 0;
        private int constMod = 0;

        private String skill = "";

        private String guildID = "none";

        // Static method for creating a builder class to build using required fields
        public static UserBuilder createUser(String username, String password) {
            UserBuilder builder = new UserBuilder();
            builder.username = username;
            builder.password = password;
            return builder;
        }

        public UserBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder setDisplayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public UserBuilder setColor(int colorID, boolean hairColor) {
            int idToSet = colorID;
            if (idToSet > 3) {
                idToSet = 3;
            } else if (idToSet < 0) {
                idToSet = 0;
            } else {
                idToSet = colorID;
            }

            if (hairColor) {
                this.hairColorID = idToSet;
            } else {
                this.colorID = idToSet;
            }
            return this;
        }

        public UserBuilder setSpecialty(int specialID, boolean race) {
            int idToSet = specialID;
            if (idToSet > 2) {
                idToSet = 2;
            } else if (idToSet < 0) {
                idToSet = 0;
            } else {
                idToSet = specialID;
            }

            if (race) {
                this.raceID = idToSet;
            } else {
                this.classID = idToSet;
            }
            return this;
        }

        public UserBuilder setGender(boolean isMale) {
            this.gender = isMale ? 1 : 0;
            return this;
        }

        public UserBuilder setMoney(double money) {
            this.money = money;
            return this;
        }

        public UserBuilder setMana(double mana) {
            this.mana = mana;
            return this;
        }

        public UserBuilder setCurrentHP(int hp) {
            this.hp = hp;
            return this;
        }

        public UserBuilder setXP(int xp) {
            this.xp = xp;
            return this;
        }

        public UserBuilder setGuildID(String guildID) {
            this.guildID = guildID;
            return this;
        }

        public User build(String... args) {
            if (this.displayName == null || this.displayName.isEmpty())
                this.displayName = this.username;

            if (this.email == null || this.email.isEmpty())
                this.email = "";

            User user = new User();
            user.username = this.username;
            user.displayName = this.displayName;
            user.password = this.password;
            user.email = this.email;
            if (args.length > 0) {
                user.id = args[0];
            } else {
                user.id = Client.getUniqueUserID();
            }

            user.hairColorID = this.hairColorID;
            user.colorID = this.colorID;
            user.raceID = this.raceID;
            user.classID = this.classID;

            user.xp = this.xp;

            Log.v("RACE", "" + this.raceID);
            Log.v("CLASS", "" + this.classID);

            int[] stats = generateStats(this.raceID, this.classID);
            user.baseStrength = stats[0];
            user.baseDext = stats[1];
            user.baseIntel = stats[2];
            user.baseConst = stats[3];
            user.strengthMod = this.strengthMod;
            user.dextMod = this.dextMod;
            user.intelMod = this.intelMod;
            user.constMod = this.constMod;

            user.maxHp = calculateStat(convertToLevel(this.xp), user.baseConst + user.constMod); // Calculate HP using constitution stat
            user.maxMana = calculateStat(convertToLevel(this.xp), user.baseIntel + user.intelMod); // Calculate Mana using intelligence stat

            user.hp = (this.hp > user.maxHp || this.hp < 0) ? user.maxHp : this.hp; // Using max mana as
            user.mana = (this.mana > user.maxMana || this.mana < 0) ? user.maxMana : this.mana;

            if (user.classID == 0) {
                user.skill = Abilities.ABILITIES.get("Cut Down");
            } else if (user.classID == 1) {
                user.skill = Abilities.ABILITIES.get("Deadeye");
            } else {
                user.skill = Abilities.ABILITIES.get("Fire Ball");
            }

            user.guildID = this.guildID;

            return user;
        }

    }

}
