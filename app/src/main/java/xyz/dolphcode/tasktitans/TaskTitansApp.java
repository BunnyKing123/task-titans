package xyz.dolphcode.tasktitans;

import android.app.Application;

import xyz.dolphcode.tasktitans.database.Client;

public class TaskTitansApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Client.init();
    }

}
