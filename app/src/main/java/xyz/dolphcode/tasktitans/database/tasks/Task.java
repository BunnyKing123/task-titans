package xyz.dolphcode.tasktitans.database.tasks;

import android.icu.util.Calendar;
import android.util.Log;

import xyz.dolphcode.tasktitans.database.Client;
import xyz.dolphcode.tasktitans.resources.FrequencyType;
import xyz.dolphcode.tasktitans.resources.TaskType;
import xyz.dolphcode.tasktitans.util.Util;

// Represents a Task, used for all three types of tasks
public class Task {

    private String taskOwnerID;
    private int taskType = TaskType.TASK;
    private String taskName;
    private String taskID;
    private String taskDesc;
    private String deadline;
    private int taskCount;
    private int freqType;
    private String freqData;
    private String lastFinished = "";

    public String getTaskOwnerID() { return taskOwnerID; }
    public String getTaskID() { return taskID; }
    public int getTaskType() { return taskType; }
    public String getTaskName() { return taskName; }
    public String getTaskDesc() { return taskDesc; }
    public String getDeadline() { return deadline; }
    public int getTaskCount() { return taskCount; }
    public int getFreqType() { return freqType; }
    public String getFreqData() { return freqData; }
    public String getLastFinished() { return lastFinished; }

    // Completes a task, returns true if the task has sufficiently run out of count
    public boolean finish() {
        this.taskCount--;
        if (taskCount <= 0) {
            if (taskType == TaskType.REPEAT_TASK) { // If this is a repeat task we need to specify when the last time this task was finished was, depends on task type
                Calendar calendar = Calendar.getInstance();
                switch (freqType) {
                    case FrequencyType.MONTHS:
                        lastFinished = "" + (calendar.get(Calendar.MONTH) - 1);
                        break;
                    case FrequencyType.WEEKS:
                        lastFinished = "" + (calendar.get(Calendar.WEEK_OF_YEAR));
                        break;
                    case FrequencyType.DAYS:
                        lastFinished = "" + (calendar.get(Calendar.DAY_OF_WEEK) - 1);
                        break;
                    case FrequencyType.DATE:
                        lastFinished = "" + calendar.get(Calendar.DAY_OF_YEAR);
                        break;
                }
                Client.updateTask(this);
            } else { // Otherwise just complete and remove the task
                Client.removeTask(this);
            }
            return true;
        } else {
            Client.updateTask(this);
            return false;
        }
    }

    public static class TaskBuilder {

        String taskOwnerID;
        String taskName;
        String taskID;
        String taskDesc;
        String deadline;
        String lastFinished = "";
        int taskCount;
        int taskType;
        String freqData;
        int freqType;

        private TaskBuilder() {}

        public static TaskBuilder createTask(String taskOwnerID, String taskName, Calendar deadline) {
            TaskBuilder builder = new TaskBuilder();
            builder.taskOwnerID = taskOwnerID;
            builder.taskName = taskName;
            builder.deadline = Util.formatDateTimeDB(deadline);
            builder.taskType = TaskType.TASK;
            return builder;
        }

        public static TaskBuilder createTask(String taskOwnerID, String taskName, String deadline) {
            TaskBuilder builder = new TaskBuilder();
            builder.taskOwnerID = taskOwnerID;
            builder.taskName = taskName;
            builder.deadline = deadline;
            builder.freqData = "";
            builder.freqType = -1;
            builder.taskType = TaskType.TASK;
            return builder;
        }

        public static TaskBuilder createGroupTask(String taskOwnerID, String taskName, Calendar deadline) {
            TaskBuilder builder = new TaskBuilder();
            builder.taskOwnerID = taskOwnerID;
            builder.taskName = taskName;
            builder.deadline = Util.formatDateTimeDB(deadline);
            builder.taskType = TaskType.TASK;
            return builder;
        }

        public static TaskBuilder createGroupTask(String taskOwnerID, String taskName, String deadline) {
            TaskBuilder builder = new TaskBuilder();
            builder.taskOwnerID = taskOwnerID;
            builder.taskName = taskName;
            builder.deadline = deadline;
            builder.freqData = "";
            builder.freqType = -1;
            builder.taskType = TaskType.GROUP_TASK;
            return builder;
        }

        public static TaskBuilder createRepeatTask(String taskOwnerID, String taskName, String freqData, int frequencyType) {
            TaskBuilder builder = new TaskBuilder();
            builder.taskOwnerID = taskOwnerID;
            builder.taskName = taskName;
            builder.deadline = "";
            builder.freqData = freqData;
            builder.freqType = frequencyType;
            builder.taskType = TaskType.REPEAT_TASK ;
            return builder;
        }

        public TaskBuilder setCount(int count) {
            this.taskCount = count < 1 ? 1 : count;
            return this;
        }

        public TaskBuilder setDesc(String desc) {
            this.taskDesc = desc;
            return this;
        }

        public TaskBuilder setLastFinished(String lastFinished) {
            this.lastFinished = lastFinished;
            return this;
        }

        public Task build(String... args) {
            Task task = new Task();

            task.taskOwnerID = this.taskOwnerID;
            task.taskName = this.taskName;
            task.taskDesc = this.taskDesc;
            task.deadline = this.deadline;
            task.taskCount = this.taskCount;
            task.taskType = this.taskType;
            task.freqData = this.freqData;
            task.freqType = this.freqType;
            task.lastFinished = this.lastFinished;

            task.taskID = args.length > 0 ? args[0] : Client.getUniqueTaskID();

            return task;
        }

    }
}
