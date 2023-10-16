package com.yousentech.roompagingexample.db;

import android.content.Context;

import androidx.room.*;
import androidx.room.migration.AutoMigrationSpec;

import com.yousentech.roompagingexample.db.dao.TaskDao;
import com.yousentech.roompagingexample.db.entity.TaskEntity;
import com.yousentech.roompagingexample.db.entity.TaskEntityFts;

@Database(entities = {TaskEntity.class, TaskEntityFts.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();

    private static volatile AppDatabase sInstance;
    private static final String DATABASE_NAME = "task_database";

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (AppDatabase.class) {
                if (sInstance == null) {
                    sInstance = Room
                            .databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                            .build();
                }
            }
        }
        return sInstance;
    }
    @RenameColumn(tableName = "task_table", fromColumnName = "rowid", toColumnName = "id")
    static class Migrate implements AutoMigrationSpec {}

}
