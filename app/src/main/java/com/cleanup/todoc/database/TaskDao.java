package com.cleanup.todoc.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.cleanup.todoc.model.Task;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM Task ORDER BY name ASC ")
    LiveData<List<Task>> getTaskByAZ();

    @Query("SELECT * FROM Task ORDER BY name DESC ")
    LiveData<List<Task>> getTaskByZA();

    @Query("SELECT * FROM Task ORDER BY creationTimestamp DESC ")
    LiveData<List<Task>> getTaskByNewer();

    @Query("SELECT * FROM Task ORDER BY creationTimestamp ASC ")
    LiveData<List<Task>> getTaskByOlder();

    @Query("SELECT * FROM Task")
    LiveData<List<Task>> getAllTask();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTask(Task task);

    @Query("DELETE FROM Task WHERE id = :id")
    void deleteTask(long id);
}
