package xyz.dolphcode.tasktitans.database.guilds;

import java.util.Calendar;
import java.util.HashMap;

import xyz.dolphcode.tasktitans.util.Util;

public class GuildTask {

    public static final int BOSS = 0;
    public static final int TASK = 1;
    public static final String[] GUILD_TASKS = {"Pick some flowers", "Take a walk in the park", "Reorganize your workspace", "Say something kind to a stranger"};
    public static final HashMap<String, Integer> BOSSES = new HashMap<String, Integer>();

    static {
        BOSSES.put("The Procrastinator", 300);
        BOSSES.put("The Weary Wraith", 250);
        BOSSES.put("The Lochness of Laziness", 320);
    }

    private int taskType;
    private String taskName;
    private String deadline;

    protected GuildTask() {
        taskType = (int) Math.round(Math.random());
        if (taskType == BOSS) {
            int index = (int) Math.floor(Math.random() * BOSSES.keySet().size());
            int counter = 0;
            for (String key:BOSSES.keySet()) {
                if (counter == index) {
                    taskName = key;
                    break;
                }
                counter++;
            }
        } else {
            taskName = GUILD_TASKS[(int) Math.floor(Math.random() * GUILD_TASKS.length)];
        }
        Calendar cal = Calendar.getInstance();
        deadline = Util.formatDate(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH), cal.get(Calendar.YEAR));
    }

    public int getTaskType() { return taskType; }
    public String getTaskName() { return taskName; }
    public String getDeadline() { return deadline; }

}
