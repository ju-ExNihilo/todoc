package com.cleanup.todoc.ui;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.repository.ProjectDataRepository;
import com.cleanup.todoc.repository.TaskDataRepository;

import java.util.List;
import java.util.concurrent.Executor;

public class MySaveTaskViewModel extends ViewModel {

    // REPOSITORIES
    private final TaskDataRepository taskDataRepository;
    private final ProjectDataRepository projectDataRepository;
    private final Executor executor;

    // DATA
    @Nullable
    private LiveData<List<Task>> currentTask;

    public MySaveTaskViewModel(TaskDataRepository taskDataRepository, ProjectDataRepository projectDataRepository, Executor executor) {
        this.taskDataRepository = taskDataRepository;
        this.projectDataRepository = projectDataRepository;
        this.executor = executor;
    }

    public void init() {
        if (this.currentTask != null) {
            return;
        }
        currentTask = taskDataRepository.getAllTask();
    }

    // -------------
    // FOR Project
    // -------------

    /** GET **/
    public LiveData<Project> getProject(long id) { return projectDataRepository.getProject(id);}

    public LiveData<List<Project>> getAllProject() { return projectDataRepository.getAllProject();}

    // -------------
    // FOR BOOKS
    // -------------

    /** GET **/
    public LiveData<List<Task>> getTaskByAZ(){return taskDataRepository.getTaskByAZ();}
    public LiveData<List<Task>> getTaskByZA(){return taskDataRepository.getTaskByZA();}
    public LiveData<List<Task>> getTaskByNewer(){return taskDataRepository.getTaskByNewer();}
    public LiveData<List<Task>> getTaskByOlder(){return taskDataRepository.getTaskByOlder();}
    public LiveData<List<Task>> getAllTask(){return taskDataRepository.getAllTask();}

    /** INSERT **/
    public void insertTask(Task task) {
        executor.execute(() -> {
            taskDataRepository.insertTask(task);
        });
    }

    /** DELETE **/
    public void deleteTask(long id) {
        executor.execute(() -> {
            taskDataRepository.deleteTask(id);
        });
    }

}