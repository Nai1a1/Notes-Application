package com.example.notesapplication;

import com.google.firebase.Timestamp;

import java.util.Calendar;

public class TaskModel {

    private String task;
    private String dueDate;
    private int status;



    String taskID;
    Timestamp timestamp;

    public TaskModel(String task, String dueDate, int status, Timestamp timestamp) {
        this.task = task;
        this.dueDate = dueDate;
        this.status = status;
        this.timestamp = timestamp;
    }
    public  TaskModel(){

    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }
}
