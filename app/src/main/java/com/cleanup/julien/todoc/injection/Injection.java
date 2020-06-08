package com.cleanup.julien.todoc.injection;

import android.content.Context;
import com.cleanup.julien.todoc.database.TodocDatabase;
import com.cleanup.julien.todoc.factory.ViewModelFactory;
import com.cleanup.julien.todoc.repository.ProjectDataRepository;
import com.cleanup.julien.todoc.repository.TaskDataRepository;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Injection {

    public static ProjectDataRepository provideProjectDataSource(Context context) {
        TodocDatabase database = TodocDatabase.getInstance(context);
        return new ProjectDataRepository(database.projectDao());
    }

    public static TaskDataRepository provideTaskDataSource(Context context) {
        TodocDatabase database = TodocDatabase.getInstance(context);
        return new TaskDataRepository(database.taskDao());
    }

    public static Executor provideExecutor(){ return Executors.newSingleThreadExecutor(); }

    public static ViewModelFactory provideDaoViewModelFactory(Context context) {
        TaskDataRepository dataSourceTask = provideTaskDataSource(context);
        ProjectDataRepository dataSourceProject = provideProjectDataSource(context);
        Executor executor = provideExecutor();
        return new ViewModelFactory(dataSourceTask, dataSourceProject, executor);
    }
}
