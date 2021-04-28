package xyz.dolphcode.tasktitans.database;

// The DatabaseObserver interface should be implemented by an activity that should be notified when the database is updated
public interface DatabaseObserver {

    public void databaseChanged(); // An implementor of this interface can perform some algorithm when the database has been modified

}
