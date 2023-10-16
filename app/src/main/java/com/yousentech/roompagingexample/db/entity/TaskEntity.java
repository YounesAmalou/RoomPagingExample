package com.yousentech.roompagingexample.db.entity;

import androidx.annotation.NonNull;
import androidx.room.*;


@Entity(tableName = "task_table")
public class TaskEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "task_name")
    public String taskName;

    @ColumnInfo(name= "task_status")
    public boolean taskStatus;

    public TaskEntity(String taskName, boolean taskStatus) {
        this.taskName = taskName;
        this.taskStatus = taskStatus;
    }

    public String getTaskName() {
        return taskName;
    }

    public boolean getTaskStatus() {
        return taskStatus;
    }

    public int getId() {
        return id;
    }

    public void setTaskStatus(boolean taskStatus) {
        this.taskStatus = taskStatus;
    }

    @NonNull
    public String toString() {
        return "TaskEntity{" +
                "id=" + id +
                ", taskName='" + taskName + '\'' +
                ", taskStatus=" + taskStatus +
                '}';
    }
}