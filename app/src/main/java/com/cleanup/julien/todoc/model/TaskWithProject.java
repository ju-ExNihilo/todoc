package com.cleanup.julien.todoc.model;

public class TaskWithProject {
    private long idTask;
    private String name;
    private long creationTimestamp;
    private long projectId;
    private String nameProject;
    private int color;

    public TaskWithProject(long idTask, String name, long creationTimestamp, long projectId, String nameProject, int color) {
        this.idTask = idTask;
        this.name = name;
        this.creationTimestamp = creationTimestamp;
        this.projectId = projectId;
        this.nameProject = nameProject;
        this.color = color;
    }

    public long getIdTask() {
        return idTask;
    }

    public String getName() {
        return name;
    }

    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    public long getProjectId() {
        return projectId;
    }

    public String getNameProject() {
        return nameProject;
    }

    public int getColor() {
        return color;
    }
}
