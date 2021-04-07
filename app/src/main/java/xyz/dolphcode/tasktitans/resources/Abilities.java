package xyz.dolphcode.tasktitans.resources;

import java.util.HashMap;

public final class Abilities {

    public static String CUT_DOWN = "Cut Down";
    public static String DEADEYE = "Deadeye";
    public static String FIRE_BALL = "Fire Ball";

    public static HashMap<String, String> ABILITIES = new HashMap<String, String>();

    static {
        ABILITIES.put("Cut Down", "The user strikes their foe with a sword");
        ABILITIES.put("Deadeye", "The user fires an arrow with deadly precision");
        ABILITIES.put("Fire Ball", "The user forms a small fire ball and launches it at the enemy");
    }

}
