package com.yousentech.roompagingexample.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Fts4;
import androidx.room.PrimaryKey;

@Fts4(contentEntity = TaskEntity.class)
@Entity(tableName = "task_table_fts")
public class TaskEntityFts {
    @PrimaryKey
    @ColumnInfo(name = "rowid")
    public int id;

    @ColumnInfo(name = "task_name")
    public String taskName;

    @ColumnInfo(name= "task_status")
    public boolean taskStatus;
}
