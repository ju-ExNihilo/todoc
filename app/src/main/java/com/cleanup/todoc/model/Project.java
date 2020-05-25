package com.cleanup.todoc.model;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * <p>Models for project in which tasks are included.</p>
 *
 * @author Gaëtan HERFRAY
 */
@Entity
public class Project {

    /**
     * The unique identifier of the project
     */
    @PrimaryKey(autoGenerate = true)
    private long id;

    /**
     * The name of the project
     */
    @NonNull
    private String nameProject;

    /**
     * The hex (ARGB) code of the color associated to the project
     */
    @ColorInt
    private int color;

    public Project() {}

    /**
     * Instantiates a new Project.
     *
     * @param nameProject  the name of the project to set
     * @param color the hex (ARGB) code of the color associated to the project to set
     */
    public Project(@NonNull String nameProject, @ColorInt int color) {
        this.nameProject = nameProject;
        this.color = color;
    }


    /**
     * Returns the unique identifier of the project.
     *
     * @return the unique identifier of the project
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the project.
     *
     * @param id the unique identifier of the task to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Returns the name of the project.
     *
     * @return the name of the project
     */
    @NonNull
    public String getNameProject() {
        return nameProject;
    }
    public void setNameProject(String nameProject) {
        this.nameProject = nameProject;
    }

    /**
     * Returns the hex (ARGB) code of the color associated to the project.
     *
     * @return the hex (ARGB) code of the color associated to the project
     */
    @ColorInt
    public int getColor() {
        return color;
    }
    public void setColor(int color) {
        this.color = color;
    }

    @Override
    @NonNull
    public String toString() {
        return getNameProject();
    }
}
