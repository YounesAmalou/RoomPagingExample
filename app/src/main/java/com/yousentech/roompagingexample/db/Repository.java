package com.yousentech.roompagingexample.db;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.PagingLiveData;

import com.yousentech.roompagingexample.db.dao.TaskDao;
import com.yousentech.roompagingexample.db.entity.TaskEntity;

public class Repository {
    private final TaskDao taskDao;

    public Repository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        taskDao = database.taskDao();
    }

    public void insert(TaskEntity task) {
        new Thread(() -> taskDao.insert(task)).start();
    }

    public LiveData<PagingData<TaskEntity>> getAllPaged() {
        Pager<Integer, TaskEntity> pager = new Pager<>(new PagingConfig(10, 20, true), taskDao::getAllPaged);
        return PagingLiveData.getLiveData(pager);
    }

    public LiveData<PagingData<TaskEntity>> getAllPaged(String search) {
        String filteredSearch = search.replaceAll("['\"\\-?*\\\\]|(?i)\\b(and|or)\\b\n", "");
        Pager<Integer, TaskEntity> pager = new Pager<>(new PagingConfig(10, 20, true), () -> taskDao.getAllPaged(filteredSearch));
        return PagingLiveData.getLiveData(pager);
    }

    public void deleteTask(TaskEntity task) {
        new Thread(() -> taskDao.delete(task)).start();
    }

    public void deleteAllTasks() {
        new Thread(taskDao::deleteAll).start();
    }

    public void updateTask(TaskEntity task) {
        new Thread(() -> taskDao.update(task)).start();
    }

    public boolean taskExists(String taskName) {
        final boolean[] result = new boolean[1];
        Thread thread = new Thread(() -> result[0] = taskDao.getTaskWithName(taskName));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return true;
        }
        return result[0];
    }
}
