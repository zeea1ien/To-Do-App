package com.example.myapplication.Model;
public class TodoItem {

    int id;
    String taskName;
    boolean status;
    String taskDate;

    public TodoItem(int id, String taskName, boolean status, String taskDate) {
        this.id = id;
        this.taskName = taskName;
        this.status = status;
        this.taskDate = taskDate ;
    }

    //region getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getTaskDate() {
        return taskDate;
    }

    //endregion
}
