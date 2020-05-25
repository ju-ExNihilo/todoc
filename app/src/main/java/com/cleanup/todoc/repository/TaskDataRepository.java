package com.cleanup.todoc.repository;

import androidx.lifecycle.LiveData;
import com.cleanup.todoc.database.TaskDao;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.model.TaskWithProject;
import java.util.List;

public class TaskDataRepository {

    private final TaskDao taskDao;

    public TaskDataRepository(TaskDao taskDao) {this.taskDao = taskDao;}

    public LiveData<List<TaskWithProject>> getTaskByAZ(){return this.taskDao.getTaskByAZ();}

    public LiveData<List<TaskWithProject>> getTaskByZA(){return this.taskDao.getTaskByZA();}

    public LiveData<List<TaskWithProject>> getTaskByNewer(){return this.taskDao.getTaskByNewer();}

    public LiveData<List<TaskWithProject>> getTaskByOlder(){return this.taskDao.getTaskByOlder();}

    public LiveData<List<TaskWithProject>> getAllTask(){return this.taskDao.getAllTask();}

    public void insertTask(Task task){this.taskDao.insertTask(task);}

    public void deleteTask(long id){this.taskDao.deleteTask(id);}
}
