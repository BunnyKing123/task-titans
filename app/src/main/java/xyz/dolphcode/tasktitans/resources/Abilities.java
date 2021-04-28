package xyz.dolphcode.tasktitans.resources;

import java.util.HashMap;

// Contains a hashmap of all the abilities a user can have
public final class Abilities {

    private Abilities() {}

    // I used a hashmap because I want to be able to identify the abilities by name
    public static HashMap<String, String> ABILITIES = new HashMap<String, String>();

    static {
        ABILITIES.put("Cut Down", "The user strikes their foe with a sword");
        ABILITIES.put("Deadeye", "The user fires an arrow with deadly precision");
        ABILITIES.put("Fire Ball", "The user forms a small fire ball and launches it at the enemy");
    }

}
