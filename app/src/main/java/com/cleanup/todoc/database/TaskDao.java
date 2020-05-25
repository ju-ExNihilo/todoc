package com.cleanup.todoc.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.model.TaskWithProject;
import java.util.List;

@Dao
public interface TaskDao {

    @Query("SELECT idTask, name, creationTimestamp, projectId, nameProject, color FROM Task " +
            "INNER JOIN project ON task.projectid = project.id ORDER BY name ASC ")
    LiveData<List<TaskWithProject>> getTaskByAZ();

    @Query("SELECT idTask, name, creationTimestamp, projectId, nameProject, color FROM Task " +
            "INNER JOIN project ON task.projectid = project.id ORDER BY name DESC ")
    LiveData<List<TaskWithProject>> getTaskByZA();

    @Query("SELECT idTask, name, creationTimestamp, projectId, nameProject, color FROM Task " +
            "INNER JOIN project ON task.projectid = project.id ORDER BY creationTimestamp DESC ")
    LiveData<List<TaskWithProject>> getTaskByNewer();

    @Query("SELECT idTask, name, creationTimestamp, projectId, nameProject, color FROM Task " +
            "INNER JOIN project ON task.projectid = project.id ORDER BY creationTimestamp ASC ")
    LiveData<List<TaskWithProject>> getTaskByOlder();

    @Query("SELECT idTask, name, creationTimestamp, projectId, nameProject, color FROM Task INNER JOIN project\n" +
            "ON task.projectid = project.id")
    LiveData<List<TaskWithProject>> getAllTask();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTask(Task task);

    @Query("DELETE FROM Task WHERE idTask = :id")
    void deleteTask(long id);
}
