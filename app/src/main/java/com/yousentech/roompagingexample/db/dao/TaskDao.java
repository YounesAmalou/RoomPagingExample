package com.yousentech.roompagingexample.db.dao;

import androidx.lifecycle.LiveData;
import androidx.paging.PagingSource;
import androidx.room.*;

import com.yousentech.roompagingexample.db.entity.TaskEntity;

import java.util.List;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM task_table")
    LiveData<List<TaskEntity>> getAll();

    @Query("SELECT * FROM task_table WHERE task_status = :status")
    LiveData<List<TaskEntity>> getAllByStatus(boolean status);

    @Insert
    void insert(TaskEntity taskEntity);

    @Update
    void update(TaskEntity taskEntity);

    @Delete
    void delete(TaskEntity taskEntity);

    @Query("DELETE FROM task_table")
    void deleteAll();

    @Query("SELECT * FROM task_table ORDER BY task_name ASC")
    PagingSource<Integer, TaskEntity> getAllPaged();

    @Query("SELECT * FROM task_table JOIN task_table_fts ON task_table.id = task_table_fts.rowid WHERE task_table_fts.task_name MATCH '*' || :search || '*' ORDER BY task_name")
    PagingSource<Integer, TaskEntity> getAllPaged(String search);

    @Query("SELECT EXISTS(SELECT 1 FROM task_table JOIN task_table_fts ON task_table.id = task_table_fts.rowid WHERE task_table_fts.task_name MATCH :taskName)")
    boolean getTaskWithName(String taskName);
}
