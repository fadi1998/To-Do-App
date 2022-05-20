package fadiebraheem.utils;

import java.util.HashMap;

enum TaskStatus{
    DONE,
    IN_PROGRESS
}

public class Task {

    //region Properties
    private String content;
    private String date;
    private TaskStatus status;
    public static HashMap<String,Task> tasks = new HashMap<>();
    //endregion

    //region Constructors
    public Task(String content, String date) {
        this.content = content;
        this.date = date;
        this.status = TaskStatus.IN_PROGRESS;
    }

    public Task(String content, String date,TaskStatus status) {
        this.content = content;
        this.date = date;
        this.status = status;
    }
    //endregion

    //region Getters And Setters
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public String toString(){
        return this.content + "\n" + this.date +"\n" + this.status +"\n\n";
    }
    //endregion

    //region Methods

    //This Method Convert Task Object Into a HashMap
    //And Returns A HashMap
    public HashMap<String,Object> toMap(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("content",this.content);
        map.put("date", this.date);
        map.put("status",this.status.toString());

        return map;
    }
    //endregion
}