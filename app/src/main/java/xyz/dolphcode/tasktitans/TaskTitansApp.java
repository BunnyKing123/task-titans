package xyz.dolphcode.tasktitans;

import android.app.Application;
import android.icu.util.Calendar;

import xyz.dolphcode.tasktitans.database.Client;

public class TaskTitansApp extends Application {

    public static Calendar TODAY;

    @Override
    public void onCreate() {
        super.onCreate();
        TODAY = Calendar.getInstance();
        Client.init(); // Make database content readable as soon as the app starts
    }

}
