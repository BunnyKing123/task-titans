package xyz.dolphcode.tasktitans.resources;

public final class TaskType {

    private TaskType() {} // Private constructor because this class should not be instantiated

    // Task type determines how a task object will be used and interpreted
    public static final int TASK = 0;
    public static final int REPEAT_TASK = 1;
    public static final int GROUP_TASK = 2;

}
