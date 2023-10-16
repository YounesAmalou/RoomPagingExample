package com.yousentech.roompagingexample.ui.allTasks;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.PagingData;
import androidx.paging.PagingLiveData;

import com.yousentech.roompagingexample.db.Repository;
import com.yousentech.roompagingexample.db.entity.TaskEntity;

import kotlinx.coroutines.CoroutineScope;

public class AllTasksViewModel extends AndroidViewModel {
    private final Repository repository;
    private final CoroutineScope viewModelScope;
    public AllTasksViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        viewModelScope = ViewModelKt.getViewModelScope(this);
    }
    public LiveData<PagingData<TaskEntity>> getTaskList() {
        return PagingLiveData.cachedIn(repository.getAllPaged(), viewModelScope);
    }
    public LiveData<PagingData<TaskEntity>> getTaskList(String search) {
        if (search.equals("")) {
            return PagingLiveData.cachedIn(repository.getAllPaged(), viewModelScope);
        }
        return PagingLiveData.cachedIn(repository.getAllPaged(search), viewModelScope);
    }

    public void insert(TaskEntity task) {
        repository.insert(task);
    }

    public void deleteTask(TaskEntity task) {
        repository.deleteTask(task);
    }

    public void deleteAllTasks() {
        repository.deleteAllTasks();
    }

    public void updateTask(TaskEntity task) {
        repository.updateTask(task);
    }

    public boolean taskExists(String taskName) {
        return repository.taskExists(taskName);
    }
}