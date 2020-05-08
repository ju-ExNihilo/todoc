package com.cleanup.todoc.repository;

import androidx.lifecycle.LiveData;

import com.cleanup.todoc.database.TaskDao;
import com.cleanup.todoc.model.Task;

import java.util.List;

public class TaskDataRepository {

    private final TaskDao taskDao;

    public TaskDataRepository(TaskDao taskDao) {this.taskDao = taskDao;}

    public LiveData<List<Task>> getTaskByAZ(){return this.taskDao.getTaskByAZ();}

    public LiveData<List<Task>> getTaskByZA(){return this.taskDao.getTaskByZA();}

    public LiveData<List<Task>> getTaskByNewer(){return this.taskDao.getTaskByNewer();}

    public LiveData<List<Task>> getTaskByOlder(){return this.taskDao.getTaskByOlder();}

    public LiveData<List<Task>> getAllTask(){return this.taskDao.getAllTask();}

    public void insertTask(Task task){this.taskDao.insertTask(task);}

    public void deleteTask(long id){this.taskDao.deleteTask(id);}
}
