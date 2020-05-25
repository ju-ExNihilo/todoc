package com.cleanup.todoc.repository;

import androidx.lifecycle.LiveData;
import com.cleanup.todoc.database.ProjectDao;
import com.cleanup.todoc.model.Project;
import java.util.List;

public class ProjectDataRepository {

    private final ProjectDao projectDao;

    public ProjectDataRepository(ProjectDao projectDao) { this.projectDao = projectDao;}

    public LiveData<List<Project>> getAllProject(){return this.projectDao.getAllProject();}

    public LiveData<Project> getProject(long id){return this.projectDao.getProject(id);}

    public void insertProject(Project project){ this.projectDao.insertProject(project);}


}
