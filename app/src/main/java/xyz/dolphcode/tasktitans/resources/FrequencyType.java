package xyz.dolphcode.tasktitans.resources;

public final class FrequencyType {

    private FrequencyType() {} // Private constructor because this class should not be instantiated

    // Constants representing different frequency types
    // The frequency type determines how the Task's frequency data will be read
    public static final int DAYS = 0; // Task will be active on certain days of the week
    public static final int MONTHS = 1; // Task will be active on certain months of the year
    public static final int DATE = 2; // Task will be active on a specific date each year
    public static final int WEEKS = 3; // Task will be active every set number of weeks

}
