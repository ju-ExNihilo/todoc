package com.cleanup.todoc.ui;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.model.TaskWithProject;
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
    private LiveData<List<TaskWithProject>> currentTask;
    private LiveData<List<Project>> currentProject;
    public static MainActivity.SortMethod SORT_METHOD = MainActivity.SortMethod.NONE;

    public MySaveTaskViewModel(TaskDataRepository taskDataRepository, ProjectDataRepository projectDataRepository, Executor executor) {
        this.taskDataRepository = taskDataRepository;
        this.projectDataRepository = projectDataRepository;
        this.executor = executor;
    }

    public void init(MainActivity.SortMethod sortMethod) {
        if (this.currentTask != null && this.currentProject != null && SORT_METHOD.equals(sortMethod)) {
            return;
        }
        SORT_METHOD = sortMethod;
        this.currentTask = taskDataRepository.getAllTask();
        this.currentProject = projectDataRepository.getAllProject();
    }

    // -------------
    // FOR Project
    // -------------

    /** GET **/
    public LiveData<Project> getProject(long id) { return projectDataRepository.getProject(id);}

    public LiveData<List<Project>> getAllProject() { return projectDataRepository.getAllProject();}

    /** INSERT **/
    public void insertProject(Project project){projectDataRepository.insertProject(project);}

    // -------------
    // FOR Task
    // -------------

    /** GET **/
    public LiveData<List<TaskWithProject>> getTaskByAZ(){return taskDataRepository.getTaskByAZ();}
    public LiveData<List<TaskWithProject>> getTaskByZA(){return taskDataRepository.getTaskByZA();}
    public LiveData<List<TaskWithProject>> getTaskByNewer(){return taskDataRepository.getTaskByNewer();}
    public LiveData<List<TaskWithProject>> getTaskByOlder(){return taskDataRepository.getTaskByOlder();}
    public LiveData<List<TaskWithProject>> getAllTask(){return taskDataRepository.getAllTask();}

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
