package com.cleanup.todoc.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.cleanup.todoc.repository.ProjectDataRepository;
import com.cleanup.todoc.repository.TaskDataRepository;
import com.cleanup.todoc.ui.MySaveTaskViewModel;
import java.util.concurrent.Executor;

public class ViewModelFactory implements ViewModelProvider.Factory{

    private final TaskDataRepository taskDataRepository;
    private final ProjectDataRepository projectDataRepository;
    private final Executor executor;

    public ViewModelFactory(TaskDataRepository taskDataRepository, ProjectDataRepository projectDataRepository, Executor executor) {
        this.taskDataRepository = taskDataRepository;
        this.projectDataRepository = projectDataRepository;
        this.executor = executor;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MySaveTaskViewModel.class)) {
            return (T) new MySaveTaskViewModel(taskDataRepository, projectDataRepository, executor);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
