package com.cleanup.todoc.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.cleanup.todoc.model.Project;
import java.util.List;

@Dao
public interface ProjectDao {

    @Query("SELECT * FROM Project ")
    LiveData<List<Project>> getAllProject();

    @Query("SELECT * FROM Project WHERE id = :id")
    LiveData<Project> getProject(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProject(Project project);

}
